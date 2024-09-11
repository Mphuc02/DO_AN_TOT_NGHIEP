package dev.workingschedule.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.workingschedule.dto.request.CreateWorkingScheduleRequest;
import dev.workingschedule.dto.request.UpdateWorkingScheduleRequest;
import dev.workingschedule.service.WorkingScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(WORKING_SCHEDULE_URL.URL)
public class WorkingScheduleRest {
    private final WorkingScheduleService workingScheduleService;

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody CreateWorkingScheduleRequest request,
                                       BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), WORKING_SCHEDULE_EXCEPTION.FAIL_VALIDATION_SCHEDULE);
        }
        return ResponseEntity.ok(workingScheduleService.save(request));
    }

    @PutMapping(WORKING_SCHEDULE_URL.ID)
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @Valid @RequestBody UpdateWorkingScheduleRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), WORKING_SCHEDULE_EXCEPTION.FAIL_VALIDATION_SCHEDULE);
        }
        return ResponseEntity.ok(workingScheduleService.update(request, id));
    }
}