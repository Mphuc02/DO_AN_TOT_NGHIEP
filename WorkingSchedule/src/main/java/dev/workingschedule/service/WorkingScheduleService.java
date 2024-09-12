package dev.workingschedule.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.DuplicateException;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.util.AuditingUtil;
import dev.common.util.DateUtil;
import dev.workingschedule.dto.request.CreateWorkingScheduleRequest;
import dev.workingschedule.dto.request.SearchWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import dev.workingschedule.repository.WorkingScheduleRepository;
import dev.workingschedule.util.WorkingScheduleUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingScheduleService {
    private final WorkingScheduleRepository workingScheduleRepository;
    private final WorkingScheduleUtil workingScheduleUtil;
    private final DateUtil dateUtil;
    private final AuditingUtil auditingUtil;

    public List<WorkingScheduleCommonResponse> searchWorkingSchedule(SearchWorkingScheduleRequest request){
        return workingScheduleUtil.mapEntitiesToResponses(workingScheduleRepository.searchWorkingSchedule(request.getStartDate(), request.getEndDate(), request.getRoomId(), request.getEmployeeId()));
    }

    @Transactional
    public WorkingScheduleCommonResponse save(CreateWorkingScheduleRequest request){
        WorkingSchedule entity = workingScheduleUtil.mapCreateRequestToEntity(request);

        Date selectedDate = dateUtil.formatDateToDD_MM_YYYY(request.getDate());
        if(workingScheduleRepository.existsByRoomIdAndDate(request.getRoomId(), selectedDate)){
            throw new DuplicateException(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED);
        }

        entity = workingScheduleRepository.save(entity);
        return workingScheduleUtil.mapEntityToResponse(entity);
    }

    @Transactional
    public WorkingScheduleCommonResponse update(CreateWorkingScheduleRequest request, UUID id){
        WorkingSchedule findToUpdate = workingScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        Date selectedDate = dateUtil.formatDateToDD_MM_YYYY(request.getDate());
        if(workingScheduleRepository.existsByRoomIdAndDate(request.getRoomId(), selectedDate)){
            throw new DuplicateException(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED);
        }
        workingScheduleUtil.mapUpdateRequestToEntity(request, findToUpdate);

        Date today = new Date(new java.util.Date().getTime());
        if(findToUpdate.getDate().after(today))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);

        findToUpdate = workingScheduleRepository.save(findToUpdate);
        return workingScheduleUtil.mapEntityToResponse(findToUpdate);
    }

    @Transactional
    public void delete(UUID id){
        WorkingSchedule findToDelete = workingScheduleRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        if(!findToDelete.getEmployeeId().equals(auditingUtil.getUserLogged().getEmployeeId()))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.NOT_PERMISSION_WITH_SCHEDULE);

        Date today = new Date(new java.util.Date().getTime());
        if(findToDelete.getDate().after(today))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);
        workingScheduleRepository.deleteById(id);
    }
}