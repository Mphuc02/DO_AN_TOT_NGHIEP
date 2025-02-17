package dev.chat.chatservice.service;

import dev.chat.chatservice.entity.RelationShip;
import dev.chat.chatservice.mapper.RelationShipMapper;
import dev.chat.chatservice.repository.RelationShipRepository;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateRelationShipCommonRequest;
import dev.common.dto.response.chat.RelationShipResponse;
import dev.common.util.AuditingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RelationShipService {
    private final RelationShipRepository relationShipRepository;
    private final RelationShipMapper relationShipMapper;
    private final AuditingUtil auditingUtil;

    public Page<RelationShipResponse> findCurrentRelationShipsOfDoctor(Pageable pageable){
        UUID doctorId = auditingUtil.getUserLogged().getId();
        return relationShipRepository.findByDoctorIdOrderByLastContactDesc(doctorId, pageable)
                                        .map(relationShipMapper::mapEntityToResponse);
    }

    public Page<RelationShipResponse> findCurrentRelationShipsOfPatient(Pageable pageable){
        UUID patientId = auditingUtil.getUserLogged().getId();
        return relationShipRepository.findByPatientIdOrderByLastContactDesc(patientId, pageable)
                .map(relationShipMapper::mapEntityToResponse);
    }

    @KafkaListener(topics = KafkaTopicsConstrant.CREATE_RELATION_SHIP_TOPIC, groupId = KafkaTopicsConstrant.CHAT_GROUP)
    public void handle(CreateRelationShipCommonRequest request){
        log.info("Received request for creating relationship with patientId:" + request.getPatientId() + ", doctorId: " + request.getDoctorId());
        if(relationShipRepository.existsByDoctorIdAndPatientId(request.getDoctorId(), request.getPatientId())){
            log.info("Existed relationship between doctorId: " + request.getDoctorId() + " and patientId: " + request.getPatientId());
            return;
        }

        RelationShip relationShip = RelationShip.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .firstContact(LocalDateTime.now())
                .lastContact(LocalDateTime.now())
                .build();

        relationShipRepository.save(relationShip);
    }
}