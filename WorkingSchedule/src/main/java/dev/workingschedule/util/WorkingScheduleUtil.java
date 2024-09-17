package dev.workingschedule.util;

import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.util.AuditingUtil;
import dev.common.util.DateUtil;
import dev.workingschedule.dto.request.CreateWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkingScheduleUtil {
    private final AuditingUtil auditingUtil;
    private final DateUtil dateUtil;

    public WorkingSchedule mapCreateRequestToEntity(CreateWorkingScheduleRequest request){
        return WorkingSchedule.builder()
                .id(UUID.randomUUID())
                .date(dateUtil.formatDateToDD_MM_YYYY(request.getDate()))
                .employeeId(auditingUtil.getUserLogged().getId())
                .roomId(request.getRoomId())
                .build();
    }

    public void mapUpdateRequestToEntity(CreateWorkingScheduleRequest request, WorkingSchedule entity){
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

    public List<WorkingScheduleCommonResponse> mapEntitiesToResponses(List<WorkingSchedule> schedules){
        return schedules.stream().map(this::mapEntityToResponse).collect(Collectors.toList());
    }
}