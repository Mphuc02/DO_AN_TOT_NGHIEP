package dev.workingschedule.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.workingschedule.dto.request.SaveWorkingScheduleRequest;
import dev.workingschedule.dto.request.SearchWorkingScheduleRequest;
import dev.workingschedule.service.WorkingScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(WORKING_SCHEDULE_URL.URL)
public class WorkingScheduleRest {
    private final WorkingScheduleService workingScheduleService;

    @GetMapping(WORKING_SCHEDULE_URL.GET_SCHEDULE_TODAY_OF_EMPLOYEE)
    public ResponseEntity<WorkingScheduleCommonResponse> getScheduleTodayOfEmployee(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.getScheduleTodayOfEmployee(id));
    }

    @GetMapping(WORKING_SCHEDULE_URL.GET_SCHEDULES_IN_MONTH_OF_EMPLOYEE)
    public ResponseEntity<List<WorkingScheduleCommonResponse>> getSchedulesInMonthOfEmployee(@RequestParam(value = "year") Integer year,
                                                                                             @RequestParam(value = "month") Integer month){
        return ResponseEntity.ok(workingScheduleService.getSchedulesInMonthOfEmployee(year, month));
    }

    @GetMapping(WORKING_SCHEDULE_URL.ID)
    public ResponseEntity<WorkingScheduleCommonResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.getById(id));
    }

    @GetMapping(WORKING_SCHEDULE_URL.CHECK_SCHEDULE_TODAY)
    public ResponseEntity<Boolean> checkScheduleIsToday(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.checkScheduleIsToday(id));
    }

    @PostMapping(WORKING_SCHEDULE_URL.SEARCH)
    public ResponseEntity<Object> search(@RequestBody SearchWorkingScheduleRequest request){
        return ResponseEntity.ok(workingScheduleService.searchWorkingSchedule(request));
    }

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody SaveWorkingScheduleRequest request,
                                       BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), WORKING_SCHEDULE_EXCEPTION.FAIL_VALIDATION_SCHEDULE);
        }
        return ResponseEntity.ok(workingScheduleService.create(request));
    }

    @PutMapping(WORKING_SCHEDULE_URL.ID)
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @Valid @RequestBody SaveWorkingScheduleRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), WORKING_SCHEDULE_EXCEPTION.FAIL_VALIDATION_SCHEDULE);
        }
        return ResponseEntity.ok(workingScheduleService.update(request, id));
    }

    @DeleteMapping(WORKING_SCHEDULE_URL.ID)
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        workingScheduleService.delete(id);
        return ResponseEntity.ok("");
    }
}