package dev.workingschedule.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.BadRequestException;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.util.AuditingUtil;
import dev.workingschedule.dto.request.SaveWorkingScheduleRequest;
import dev.workingschedule.dto.request.SearchWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import dev.workingschedule.repository.WorkingScheduleRepository;
import dev.workingschedule.util.WorkingScheduleMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    public List<WorkingScheduleCommonResponse> getSchedulesInMonthOfEmployee(int year, int month){
        UUID employeeId = auditingUtil.getUserLogged().getId();
        LocalDate thisMonth = LocalDate.of(year, month, 1);
        LocalDate nextMonth = thisMonth.plusMonths(1);
        return scheduleMapperUtil.mapEntitiesToCommonResponses(scheduleRepository.findByEmployeeIdAndDateBetweenOrderByDate(employeeId, thisMonth, nextMonth));
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
    public WorkingScheduleCommonResponse create(SaveWorkingScheduleRequest request){
        WorkingSchedule entity = scheduleMapperUtil.mapCreateRequestToEntity(request);
        checkScheduleConflict(request);

        entity.setEmployeeId(auditingUtil.getUserLogged().getId());
        entity = scheduleRepository.save(entity);
        return scheduleMapperUtil.mapEntityToCommonResponse(entity);
    }

    @Transactional
    public WorkingScheduleCommonResponse update(SaveWorkingScheduleRequest request, UUID id){
        WorkingSchedule findToUpdate = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        checkScheduleConflict(request);
        scheduleMapperUtil.mapUpdateRequestToEntity(request, findToUpdate);

        LocalDate today = LocalDate.now();
        if(findToUpdate.getDate().isBefore(today))
            throw new BadRequestException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);

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
        if(findToDelete.getDate().isBefore(today))
            throw new NotPermissionException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);
        scheduleRepository.deleteById(id);
    }

    private void checkScheduleConflict(SaveWorkingScheduleRequest request){
        List<WorkingSchedule> schedulesInDay = scheduleRepository.getByDate(request.getDate());
        schedulesInDay.forEach(schedule -> {
            UUID employeeId = auditingUtil.getUserLogged().getId();

            if(Objects.equals(schedule.getRoomId(), request.getRoomId())){
                throw new BadRequestException(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED);
            }

            if(Objects.equals(schedule.getEmployeeId(), employeeId)){
                throw new BadRequestException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_CONFLICT);
            }
        });
    }
}