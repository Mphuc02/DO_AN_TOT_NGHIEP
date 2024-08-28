package dev.faculty.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.faculty.dto.request.CreatePositionRequest;
import dev.faculty.dto.request.UpdatePositionRequest;
import dev.faculty.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(FACULTY_URL.POSITION_URL)
public class PositionRest {
    private final PositionService positionService;

    @GetMapping(FACULTY_URL.FACULTY_ID)
    public ResponseEntity<Object> getByFaculty(@PathVariable UUID id){
        return ResponseEntity.ok(positionService.getByFaculty(id));
    }

    @GetMapping(FACULTY_URL.ID)
    public ResponseEntity<Object> getById(@PathVariable UUID id){
        return ResponseEntity.ok(positionService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody CreatePositionRequest request,
                                       BindingResult result){
        if(result.hasErrors())
            throw new ObjectIllegalArgumentException(result.getAllErrors(), FACULTY_EXCEPTION.FAIL_VALIDATION_POSITION);
        return ResponseEntity.ok(positionService.save(request));
    }

    @PutMapping(FACULTY_URL.ID)
    public ResponseEntity<Object> update(@Valid @RequestBody UpdatePositionRequest request,
                                         BindingResult result,
                                         @PathVariable UUID id){
        if(result.hasErrors())
            throw new ObjectIllegalArgumentException(result.getAllErrors(), FACULTY_EXCEPTION.FAIL_VALIDATION_POSITION);
        return ResponseEntity.ok(positionService.update(request, id));
    }

    @DeleteMapping(FACULTY_URL.ID)
    public String delete(@PathVariable UUID id){
        positionService.delete(id);
        return "";
    }
}