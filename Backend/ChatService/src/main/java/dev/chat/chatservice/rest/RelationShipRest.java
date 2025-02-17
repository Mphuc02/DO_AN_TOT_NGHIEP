package dev.chat.chatservice.rest;

import dev.chat.chatservice.service.RelationShipService;
import static dev.common.constant.ApiConstant.CHAT.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RELATION_SHIP_URL)
@RequiredArgsConstructor
public class RelationShipRest {
    private final RelationShipService relationShipService;

    @GetMapping(FIND_RELATION_SHIPS_OF_DOCTOR)
    public ResponseEntity<Object> findRecentRelationShipsOfDoctor(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(relationShipService.findCurrentRelationShipsOfDoctor(pageable));
    }

    @GetMapping(FIND_RELATION_SHIPS_OF_PATIENT)
    public ResponseEntity<Object> findRecentRelationShipsOfPatient(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(relationShipService.findCurrentRelationShipsOfPatient(pageable));
    }
}