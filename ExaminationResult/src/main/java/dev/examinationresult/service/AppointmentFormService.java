package dev.examinationresult.service;

import dev.common.client.WorkingScheduleClient;
import dev.common.constant.ExceptionConstant.EXAMINATION_RESULT_EXCEPTION;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.util.AuditingUtil;
import dev.examinationresult.dto.request.CreateAppointmentFormRequest;
import dev.examinationresult.dto.response.AppointmentFormResponse;
import dev.examinationresult.entity.AppointmentForm;
import dev.examinationresult.repository.AppointmentFormRepository;
import dev.examinationresult.util.AppointmentFormMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentFormService {
    private final AppointmentFormRepository appointmentRepository;
    private final AppointmentFormMapperUtil appointmentMapperUtil;
    private final WorkingScheduleClient workingScheduleClient;
    private final AuditingUtil auditingUtil;

    public AppointmentFormResponse create(CreateAppointmentFormRequest request){
        AppointmentForm form = appointmentMapperUtil.mapCreateRequestToEntity(request);

        UUID employeeId = auditingUtil.getUserLogged().getId();
        WorkingScheduleCommonResponse schedule = workingScheduleClient.getScheduleTodayOfEmployee(employeeId);
        if(schedule == null){
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.NOT_HAVE_SCHEDULE_TODAY);
        }
        form.setWorkingScheduleId(schedule.getId());
        form = appointmentRepository.save(form);
        return appointmentMapperUtil.mapEntityToResponse(form);
    }

    public void delete(UUID id){
        AppointmentForm form = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException(EXAMINATION_RESULT_EXCEPTION.APPOINTMENT_FORM_NOT_FOUND));
        LocalDateTime today = LocalDateTime.now();
        if(form.getCreatedAt().plusDays(1).isAfter(today))
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.OUT_OF_TIME_DELETE_APPOINTMENT_FORM);

        if(!Objects.equals(form.getEmployeeId(), auditingUtil.getUserLogged().getId()))
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.NOT_OWN_APPOINTMENT_FORM);

        appointmentRepository.deleteById(id);
    }
}