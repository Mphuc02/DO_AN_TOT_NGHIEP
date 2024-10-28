package dev.patient.rest;

import static dev.common.constant.ApiConstant.PATIENT.*;
import dev.common.constant.AuthorizationConstant;
import dev.common.dto.response.patient.PatientResponse;
import dev.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(PATIENT_URL)
public class PatientRest {
    private final PatientService patientService;

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll(){
        return ResponseEntity.ok(patientService.getAll());
    }

    @PreAuthorize(AuthorizationConstant.RECEIPT_ADMIN)
    @PostMapping(GET_BY_IDS)
    public ResponseEntity<List<PatientResponse>> getByIds(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(patientService.getByIds(ids));
    }

    @GetMapping(CHECK_EXIST_PATIENT)
    public ResponseEntity<Boolean> checkPatientExist(@PathVariable UUID id){
        return ResponseEntity.ok(patientService.checkPatientExist(id));
    }
}