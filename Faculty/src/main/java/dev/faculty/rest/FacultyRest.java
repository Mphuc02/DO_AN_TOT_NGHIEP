package dev.faculty.rest;

import dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.FACULTY_URL;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.faculty.dto.request.CreateFacultyRequest;
import dev.faculty.dto.request.UpdateFacultyRequest;
import dev.faculty.service.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(FACULTY_URL.URL)
public class FacultyRest {
    private final FacultyService facultyService;

    @GetMapping()
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(facultyService.getAll());
    }

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody CreateFacultyRequest request, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), HOSPITAL_INFORMATION.FAIL_VALIDATION_FACULTY);
        }
        return ResponseEntity.ok(facultyService.save(request));
    }

    @PutMapping(FACULTY_URL.ID)
    public ResponseEntity<Object> update(@PathVariable UUID id, @Valid @RequestBody UpdateFacultyRequest request, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), HOSPITAL_INFORMATION.FAIL_VALIDATION_FACULTY);
        }
        return ResponseEntity.ok(facultyService.update(request, id));
    }

    @DeleteMapping(FACULTY_URL.ID)
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        facultyService.delete(id);
        return ResponseEntity.ok("ok");
    }
}