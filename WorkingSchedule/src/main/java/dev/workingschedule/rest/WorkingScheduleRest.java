package dev.workingschedule.rest;

import static dev.common.constant.ApiConstant.WORKING_SCHEDULE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.working_schedule.WorkingScheduleResponse;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.workingschedule.dto.request.SaveWorkingScheduleRequest;
import dev.workingschedule.dto.request.SearchWorkingScheduleRequest;
import dev.workingschedule.service.WorkingScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(URL)
public class WorkingScheduleRest {
    private final WorkingScheduleService workingScheduleService;

    @GetMapping(GET_SCHEDULE_TODAY_OF_EMPLOYEE)
    public ResponseEntity<WorkingScheduleResponse> getScheduleTodayOfEmployee(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.getScheduleTodayOfEmployee(id));
    }

    @GetMapping(GET_SCHEDULES_IN_MONTH_OF_EMPLOYEE)
    public ResponseEntity<List<WorkingScheduleResponse>> getSchedulesInMonthOfEmployee(@RequestParam(value = "year") Integer year,
                                                                                       @RequestParam(value = "month") Integer month){
        return ResponseEntity.ok(workingScheduleService.getSchedulesInMonthOfEmployee(year, month));
    }

    @GetMapping(GET_SCHEDULES_BY_DATE)
    public ResponseEntity<List<WorkingScheduleResponse>> getSchedulesByDate(@RequestParam("date")LocalDate date){
        return ResponseEntity.ok(workingScheduleService.getSchedulesByDate(date));
    }

    @GetMapping(ID)
    public ResponseEntity<WorkingScheduleResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.getById(id));
    }

    @GetMapping(CHECK_SCHEDULE_TODAY)
    public ResponseEntity<Boolean> checkScheduleIsToday(@PathVariable UUID id){
        return ResponseEntity.ok(workingScheduleService.checkScheduleIsToday(id));
    }

    @PostMapping(SEARCH)
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

    @PutMapping(ID)
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @Valid @RequestBody SaveWorkingScheduleRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), WORKING_SCHEDULE_EXCEPTION.FAIL_VALIDATION_SCHEDULE);
        }
        return ResponseEntity.ok(workingScheduleService.update(request, id));
    }

    @DeleteMapping(ID)
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        workingScheduleService.delete(id);
        return ResponseEntity.ok("");
    }
}