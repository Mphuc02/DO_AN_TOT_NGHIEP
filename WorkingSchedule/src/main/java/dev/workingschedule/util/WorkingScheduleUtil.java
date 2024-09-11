package dev.workingschedule.util;

import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.util.AuditingUtil;
import dev.common.util.DateUtil;
import dev.workingschedule.dto.request.CreateWorkingScheduleRequest;
import dev.workingschedule.dto.request.UpdateWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkingScheduleUtil {
    private final AuditingUtil auditingUtil;
    private final DateUtil dateUtil;

    public WorkingSchedule mapCreateRequestToEntity(CreateWorkingScheduleRequest request){
        return WorkingSchedule.builder()
                .id(UUID.randomUUID())
                .date(dateUtil.formatDateToDD_MM_YYYY(request.getDate()))
                .employeeId(auditingUtil.getUserLogged().getEmployeeId())
                .roomId(request.getRoomId())
                .build();
    }

    public void mapUpdateRequestToEntity(UpdateWorkingScheduleRequest request, WorkingSchedule entity){
        if(!ObjectUtils.isEmpty(request.getDate()))
            entity.setDate(dateUtil.formatDateToDD_MM_YYYY(request.getDate()));

        if(!ObjectUtils.isEmpty(request.getRoomId()))
            entity.setRoomId(request.getRoomId());
    }

    public WorkingScheduleCommonResponse mapEntityToResponse(WorkingSchedule entity){
        return WorkingScheduleCommonResponse.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .employeeId(entity.getEmployeeId())
                .date(entity.getDate())
                .build();
    }
}