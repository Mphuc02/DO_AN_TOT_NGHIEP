package dev.chat.chatservice.service;

import dev.chat.chatservice.dto.request.CreateMessageRequest;
import dev.chat.chatservice.dto.request.DetectImageRequest;
import dev.chat.chatservice.entity.Message;
import dev.chat.chatservice.entity.RelationShip;
import dev.chat.chatservice.mapper.MessageMapper;
import dev.chat.chatservice.repository.MessageRepository;
import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.chat.chatservice.repository.RelationShipRepository;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.MinioConstant;
import dev.common.dto.response.chat.DetectedImageChatResponse;
import dev.common.dto.response.chat.MessageResponse;
import dev.common.exception.BaseException;
import dev.common.model.AuthenticatedUser;
import dev.common.model.Role;
import dev.common.service.S3ClientService;
import dev.common.util.AuditingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RelationShipRepository relationShipRepository;
    private final AuditingUtil auditingUtil;
    private final S3ClientService s3ClientService;

    @Value(REQUEST_DETECT_IMAGE_TOPIC)
    private String requestDetectImageTopic;

    @Value(NEW_MESSAGE_TOPIC)
    private String newMessageTopic;

    public Page<MessageResponse> getWithOtherById(UUID receiverId, Pageable pageable){
        UUID senderId = auditingUtil.getUserLogged().getId();
        Page<Message> messages = messageRepository.findByReceiverId(receiverId, senderId, pageable);
        return messages.map(messageMapper::mapEntityToResponse);
    }

    @Transactional
    public MessageResponse sendMessage(CreateMessageRequest request){
        AuthenticatedUser user = auditingUtil.getUserLogged();
        log.info("Received create message from topic with senderId: " + user.getId() + ", receiverId: " + request.getReceiverId());

        Message message = messageMapper.mapCreateRequestToEntity(request);
        message.setSenderId(user.getId());
        message.setCreatedAt(LocalDateTime.now());
        message = messageRepository.save(message);

        RelationShip relationShip = getRelationShip(user.getId(), request.getReceiverId());
        relationShip.setLastMessage(message.getContent());
        relationShipRepository.save(relationShip);

        MessageResponse response = messageMapper.mapEntityToResponse(message);
        response.setRelationShipId(relationShip.getId());
        kafkaTemplate.send(newMessageTopic, response);
        return response;
    }

    @Transactional
    public void receivedImage(MultipartFile image, UUID receiverId) throws IOException {
        AuthenticatedUser user = auditingUtil.getUserLogged();

        // Lưu file
        String originalFilename = image.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String originFile = UUID.randomUUID() + fileExtension;
        s3ClientService.uploadObject(MinioConstant.MESSAGE_IMAGE_BUCKET, image.getBytes(), originFile);

        Message message = Message.builder()
                .senderId(user.getId())
                .receiverId(receiverId)
                .imageUrl(MinioConstant.MESSAGE_IMAGE_BUCKET + "/" + originFile)
                .createdAt(LocalDateTime.now()).build();
        message = messageRepository.save(message);

        RelationShip relationShip = getRelationShip(user.getId(),receiverId);
        relationShip.messageIsImage();
        relationShip.messageIsImage();
        relationShipRepository.save(relationShip);

        DetectImageRequest detectRequest = messageMapper.mapEntityToImageRequest(message);
        detectRequest.setRelationShipId(relationShip.getId());
        detectRequest.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        kafkaTemplate.send(requestDetectImageTopic, detectRequest);

        MessageResponse response = messageMapper.mapEntityToResponse(message);
        response.setRelationShipId(relationShip.getId());
        kafkaTemplate.send(newMessageTopic, response);
    }

    private RelationShip getRelationShip(UUID senderId, UUID receiverId){
        if(auditingUtil.getUserLogged().getRoles().contains(Role.DOCTOR)){
            return relationShipRepository.findByPatientIdAndDoctorId(receiverId, senderId);
        }else{
            return relationShipRepository.findByPatientIdAndDoctorId(senderId, receiverId);
        }
    }

    @KafkaListener(topics = DETECTED_IMAGE_TOPIC, groupId = CHAT_GROUP)
    public void handle(DetectedImageChatResponse response){
        log.info("Request response from detected_image topic with id: " + response.getId());
        Message message = messageRepository.findById(response.getId()).orElseThrow(() -> BaseException.buildNotFound().message(ChatException.MESSAGE_NOT_FOUND).build());
        byte[] imageBytes = Base64.getDecoder().decode(response.getProcessedImage());

        String originFile = UUID.randomUUID() + ".jpg";
        s3ClientService.uploadObject(MinioConstant.MESSAGE_IMAGE_BUCKET, imageBytes, originFile);
        message.setDetectedImageUrl(MinioConstant.MESSAGE_IMAGE_BUCKET + "/" + originFile);

        messageRepository.save(message);

        MessageResponse messageResponse = messageMapper.mapEntityToResponse(message);
        messageResponse.setRelationShipId(response.getRelationShipId());
        kafkaTemplate.send(newMessageTopic, messageResponse);
    }
}