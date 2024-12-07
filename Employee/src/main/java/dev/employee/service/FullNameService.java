package dev.employee.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.UpdateWithFullNameRequest;
import dev.common.exception.NotFoundException;
import dev.employee.entity.FullName;
import dev.employee.repository.FullNameRepository;
import dev.employee.util.FullNameUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FullNameService {
    private final FullNameRepository fullNameRepository;
    private final FullNameUtil fullNameUtil;

    @Transactional
    public void update(UpdateWithFullNameRequest request){
        FullName findToUpdate = findById(request.getId());
        fullNameUtil.updateRequestToEntity(request, findToUpdate);
        fullNameRepository.save(findToUpdate);
    }

    private FullName findById(UUID id){
        return fullNameRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(EMPLOYEE_EXCEPTION.FULL_NAME_NOT_FOUND));
    }
}