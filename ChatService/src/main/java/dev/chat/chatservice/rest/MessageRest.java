package dev.chat.chatservice.rest;

import static dev.common.constant.ApiConstant.CHAT.*;
import dev.chat.chatservice.service.MessageService;
import dev.common.constant.AuthorizationConstant;
import dev.chat.chatservice.dto.request.CreateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController()
@RequiredArgsConstructor
@RequestMapping(CHAT_URL)
public class MessageRest {
    private final MessageService messageService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping(RECEIVER_ID)
    @PreAuthorize(AuthorizationConstant.USER)
    public ResponseEntity<Object> getMessageByReceiverId(@PathVariable UUID id, @PageableDefault Pageable pageable){
        return ResponseEntity.ok(messageService.getWithOtherById(id, pageable));
    }

    @PostMapping()
    @PreAuthorize(AuthorizationConstant.USER)
    public ResponseEntity<Object> sendMessage(@Validated @RequestBody CreateMessageRequest request){
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @PostMapping(SEND_IMAGE)
    public ResponseEntity<Object> receivedImage(@RequestPart("image") MultipartFile image, @PathVariable(value = "id") UUID receiverId) throws IOException {
        messageService.receivedImage(image, receiverId);
        return ResponseEntity.ok("");
    }
}