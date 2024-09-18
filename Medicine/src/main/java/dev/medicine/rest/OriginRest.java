package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.CreateOriginRequest;
import dev.medicine.dto.request.UpdateOriginRequest;
import dev.medicine.dto.response.OriginResponse;
import dev.medicine.service.OriginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ORIGIN_URL)
@RequiredArgsConstructor
public class OriginRest {
    private final OriginService originService;

    @GetMapping()
    public ResponseEntity<List<OriginResponse>> getAll(){
        return ResponseEntity.ok(originService.getAll());
    }

    @PostMapping()
    public ResponseEntity<OriginResponse> save(@Valid @RequestBody CreateOriginRequest request,
                                               BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_ORIGIN);
        }
        return ResponseEntity.ok(originService.save(request));
    }

    @PutMapping(ID)
    public ResponseEntity<OriginResponse> update(@Valid @RequestBody UpdateOriginRequest request,
                                                 BindingResult result,
                                                 @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_ORIGIN);
        }
        return ResponseEntity.ok(originService.update(request, id));
    }
}