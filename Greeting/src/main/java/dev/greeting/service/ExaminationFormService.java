package dev.greeting.service;

import dev.common.dto.response.ExaminationFormResponse;
import dev.common.model.AuthenticatedUser;
import dev.greeting.dto.request.CreateExaminationFormRequest;
import dev.greeting.entity.ExaminationForm;
import dev.greeting.repository.ExaminationFormRepository;
import dev.greeting.util.ExaminationFormUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExaminationFormService {
    private final ExaminationFormRepository examinationFormRepository;
    private final TicketService ticketService;
    private final ExaminationFormUtil examinationFormUtil;

    public ExaminationFormResponse saveWithoutAppointment(CreateExaminationFormRequest request){
        ExaminationForm entity = examinationFormUtil.createRequestToEntity(request);
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return null;
    }
}