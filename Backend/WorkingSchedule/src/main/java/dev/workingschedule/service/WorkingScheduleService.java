package dev.workingschedule.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.working_schedule.WorkingScheduleResponse;
import dev.common.exception.BadRequestException;
import dev.common.exception.BaseException;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.model.ErrorField;
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

    public WorkingScheduleResponse getById(UUID id){
        WorkingSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));
        return scheduleMapperUtil.mapEntityToResponse(schedule);
    }

    public List<WorkingScheduleResponse> searchWorkingSchedule(SearchWorkingScheduleRequest request){
        return scheduleMapperUtil.mapEntitiesToResponses(scheduleRepository.searchWorkingSchedule(request.getStartDate(), request.getEndDate(), request.getRoomId(), request.getEmployeeId()));
    }

    public List<WorkingScheduleResponse> getSchedulesByDate(LocalDate date){
        return scheduleMapperUtil.mapEntitiesToResponses(scheduleRepository.getByDate(date));
    }

    public WorkingScheduleResponse getScheduleTodayOfEmployee(UUID employeeId){
        LocalDate today = LocalDate.now();
        return scheduleMapperUtil.mapEntityToResponse(scheduleRepository.findByEmployeeIdAndDate(employeeId, today));
    }

    public List<WorkingScheduleResponse> getSchedulesInMonthOfEmployee(int year, int month){
        UUID employeeId = auditingUtil.getUserLogged().getId();
        LocalDate thisMonth = LocalDate.of(year, month, 1);
        LocalDate nextMonth = thisMonth.plusMonths(1);
        return scheduleMapperUtil.mapEntitiesToResponses(scheduleRepository.findByEmployeeIdAndDateBetweenOrderByDate(employeeId, thisMonth, nextMonth));
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
    public WorkingScheduleResponse create(SaveWorkingScheduleRequest request){
        WorkingSchedule entity = scheduleMapperUtil.mapCreateRequestToEntity(request);
        checkScheduleConflict(request);

        entity.setEmployeeId(auditingUtil.getUserLogged().getId());
        entity = scheduleRepository.save(entity);
        return scheduleMapperUtil.mapEntityToResponse(entity);
    }

    @Transactional
    public WorkingScheduleResponse update(SaveWorkingScheduleRequest request, UUID id){
        WorkingSchedule findToUpdate = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_NOT_FOUND));

        checkScheduleConflict(request);
        scheduleMapperUtil.mapUpdateRequestToEntity(request, findToUpdate);

        LocalDate today = LocalDate.now();
        if(findToUpdate.getDate().isBefore(today))
            throw new BadRequestException(WORKING_SCHEDULE_EXCEPTION.CAN_NOT_UPDATE_OLD_SCHEDULE);

        findToUpdate = scheduleRepository.save(findToUpdate);
        return scheduleMapperUtil.mapEntityToResponse(findToUpdate);
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
        UUID employeeId = auditingUtil.getUserLogged().getId();
        schedulesInDay.forEach(schedule -> {
            if(Objects.equals(schedule.getRoomId(), request.getRoomId())){
                ErrorField field = new ErrorField(WORKING_SCHEDULE_EXCEPTION.ROOM_HAS_BEEN_SELECTED, SaveWorkingScheduleRequest.Fields.roomId);
                throw BaseException.buildBadRequest().addField(field).build();
            }

            if(Objects.equals(schedule.getEmployeeId(), employeeId) && Objects.equals(request.getRoomId(), schedule.getRoomId())){
                ErrorField field = new ErrorField(WORKING_SCHEDULE_EXCEPTION.WORKING_SCHEDULE_CONFLICT, SaveWorkingScheduleRequest.Fields.roomId);
                throw BaseException.buildBadRequest().addField(field).build();
            }
        });
    }
}