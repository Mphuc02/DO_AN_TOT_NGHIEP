package dev.patient.rest;

import static dev.common.constant.ApiConstant.PATIENT.*;
import dev.common.constant.AuthorizationConstant;
import dev.common.dto.response.patient.AppointmentDetailResponse;
import dev.common.dto.response.patient.AppointmentImageDetailResponse;
import dev.patient.dto.request.CreateAppointmentByDoctorRequest;
import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.common.dto.response.patient.AppointmentResponse;
import dev.patient.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(APPOINTMENT_URL)
@RequiredArgsConstructor
public class AppointmentRest {
    private final AppointmentService appointmentService;

    @PreAuthorize(AuthorizationConstant.RECEIPT_ADMIN)
    @GetMapping(GET_APPOINTMENTS_OF_TODAY)
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsOfToday(){
        return ResponseEntity.ok(appointmentService.getAppointmentsOfToday());
    }

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @GetMapping(FIND_DETAILS_BY_APPOINTMENT_ID)
    public ResponseEntity<List<AppointmentImageDetailResponse>> getDetailByAppointmentID(@PathVariable UUID id){
        return ResponseEntity.ok(appointmentService.findDetailsByAppointmentId(id));
    }

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @PostMapping(DOCTOR_CREATE_APPOINTMENT)
    public ResponseEntity<AppointmentResponse> doctorCreateAppointment(@Validated @RequestBody CreateAppointmentByDoctorRequest request){
        return ResponseEntity.ok(appointmentService.create(request));
    }

    @PreAuthorize(AuthorizationConstant.USER)
    @PostMapping()
    public ResponseEntity<AppointmentResponse> create(@Validated @RequestBody CreateAppointmentRequest request){
        return ResponseEntity.ok(appointmentService.create(request));
    }

    @PreAuthorize(AuthorizationConstant.USER)
    @PutMapping(ID)
    public ResponseEntity<AppointmentResponse> update(@PathVariable UUID id, @Validated @RequestBody UpdateAppointmentRequest request){
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    @PreAuthorize(AuthorizationConstant.USER)
    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        appointmentService.delete(id);
        return null;
    }
}