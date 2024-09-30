package dev.payment.rest;

import static dev.common.constant.ApiConstant.PAYMENT.*;

import dev.common.constant.AuthorizationConstrant;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.payment.dto.request.CreateExaminationCostRequest;
import dev.payment.dto.response.ExaminationCostResponse;
import dev.payment.service.ExaminationCostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXAMINATION_COST_URL)
public class ExaminationCostRest {
    private final ExaminationCostService costService;

    @GetMapping()
    public ResponseEntity<List<ExaminationCostResponse>> getAll(){
        return ResponseEntity.ok(costService.getAll());
    }

    @PreAuthorize(AuthorizationConstrant.ADMIN)
    @PostMapping()
    public ResponseEntity<ExaminationCostResponse> create(@Valid @RequestBody CreateExaminationCostRequest request,
                                                          BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), PAYMENT_EXCEPTION.FAIL_VALIDATION_EXAMINATION_COST);
        }
        return ResponseEntity.ok(costService.create(request));
    }

    @DeleteMapping(ID)
    public ResponseEntity<String> delete(@PathVariable UUID id){
        costService.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}