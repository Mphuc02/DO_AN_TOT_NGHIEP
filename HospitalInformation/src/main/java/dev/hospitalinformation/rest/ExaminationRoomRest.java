package dev.hospitalinformation.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant;
import dev.common.dto.response.ExaminationFormResponse;
import dev.common.dto.response.ExaminationRoomCommonResponse;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.hospitalinformation.dto.request.CreateExaminationRoomRequest;
import dev.hospitalinformation.dto.request.UpdateExaminationRoomRequest;
import dev.hospitalinformation.service.ExaminationRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HOSPITAL_INFORMATION.EXAMINATION_ROOM_URL)
@RequiredArgsConstructor
public class ExaminationRoomRest {
    private final ExaminationRoomService examinationRoomService;

    @GetMapping()
    public ResponseEntity<List<ExaminationRoomCommonResponse>> getAll(){
        return ResponseEntity.ok(examinationRoomService.getAll());
    }

    @GetMapping(HOSPITAL_INFORMATION.CHECK_ROOM_EXIST)
    public ResponseEntity<Boolean> checkRoomExist(@PathVariable UUID id){
        return ResponseEntity.ok(examinationRoomService.checkRoomExist(id));
    }

    @PostMapping(HOSPITAL_INFORMATION.GET_BY_IDS)
    public ResponseEntity<List<ExaminationRoomCommonResponse>> findByIds(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(examinationRoomService.findByIds(ids));
    }

    @PostMapping()
    public ResponseEntity<ExaminationRoomCommonResponse> save(@Valid @RequestBody CreateExaminationRoomRequest request,
                                                              BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), ExceptionConstant.HOSPITAL_INFORMATION_EXCEPTION.FAIL_VALIDATION_ROOM);
        }
        return ResponseEntity.ok(examinationRoomService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExaminationRoomCommonResponse> update(@Valid @RequestBody UpdateExaminationRoomRequest request,
                                                                BindingResult result,
                                                                @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), ExceptionConstant.HOSPITAL_INFORMATION_EXCEPTION.FAIL_VALIDATION_ROOM);
        }
        return ResponseEntity.ok(examinationRoomService.update(request, id));
    }
}