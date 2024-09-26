package dev.workingschedule.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.DuplicateException;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.util.AuditingUtil;
import dev.workingschedule.dto.request.CreateWorkingScheduleRequest;
import dev.workingschedule.dto.request.SearchWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import dev.workingschedule.repository.WorkingScheduleRepository;
import dev.workingschedule.util.WorkingScheduleMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingScheduleService {
    private final WorkingScheduleRepository scheduleRepository;
    private final WorkingScheduleMapperUtil scheduleMapperUtil;
    private final AuditingUtil auditingUtil;

    public WorkingScheduleCommonResponse getById(UUID id){
        WorkingSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));
        return scheduleMapperUtil.mapEntityToCommonResponse(schedule);
    }

    public List<WorkingScheduleCommonResponse> searchWorkingSchedule(SearchWorkingScheduleRequest request){
        return scheduleMapperUtil.mapEntitiesToCommonResponses(scheduleRepository.searchWorkingSchedule(request.getStartDate(), request.getEndDate(), request.getRoomId(), request.getEmployeeId()));
    }

    public WorkingScheduleCommonResponse getScheduleTodayOfEmployee(UUID employeeId){
        LocalDate today = LocalDate.now();
        return scheduleMapperUtil.mapEntityToCommonResponse(scheduleRepository.findByEmployeeIdAndDate(employeeId, today));
    }

    public boolean checkScheduleIsToday(UUID id){
        Optional<WorkingSchedule> optional = scheduleRepository.findById(id);
        if(optional.isEmpty())
            return false;
        WorkingSchedule schedule = optional.get();
        LocalDate today = LocalDate.now();
        return schedule.getDate().equals(today);
    }

    @Transactional
    public WorkingScheduleCommonResponse save(CreateWorkingScheduleRequest request){
        WorkingSchedule entity = scheduleMapperUtil.mapCreateRequestToEntity(request);

        LocalDate selectedDate = request.getDate();
        if(scheduleRepository.existsByRoomIdAndDate(request.getRoomId(), selectedDate)){
            throw new DuplicateException(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED);
        }

        entity.setEmployeeId(auditingUtil.getUserLogged().getId());
        entity = scheduleRepository.save(entity);
        return scheduleMapperUtil.mapEntityToCommonResponse(entity);
    }

    @Transactional
    public WorkingScheduleCommonResponse update(CreateWorkingScheduleRequest request, UUID id){
        WorkingSchedule findToUpdate = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        LocalDate selectedDate = request.getDate();
        if(scheduleRepository.existsByRoomIdAndDate(request.getRoomId(), selectedDate)){
            throw new DuplicateException(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED);
        }
        scheduleMapperUtil.mapUpdateRequestToEntity(request, findToUpdate);

        LocalDate today = LocalDate.now();
        if(findToUpdate.getDate().isAfter(today))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);

        findToUpdate = scheduleRepository.save(findToUpdate);
        return scheduleMapperUtil.mapEntityToCommonResponse(findToUpdate);
    }

    @Transactional
    public void delete(UUID id){
        WorkingSchedule findToDelete = scheduleRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        if(!findToDelete.getEmployeeId().equals(auditingUtil.getUserLogged().getId()))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.NOT_PERMISSION_WITH_SCHEDULE);

        LocalDate today = LocalDate.now();
        if(findToDelete.getDate().isAfter(today))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);
        scheduleRepository.deleteById(id);
    }
}