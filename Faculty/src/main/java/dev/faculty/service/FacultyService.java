package dev.faculty.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.FacultyResponse;
import dev.common.exception.NotFoundException;
import dev.faculty.dto.request.CreateFacultyRequest;
import dev.faculty.dto.request.UpdateFacultyRequest;
import dev.faculty.entity.Faculty;
import dev.faculty.repository.FacultyRepository;
import dev.faculty.util.FacultyUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyUtil facultyUtil;

    public List<FacultyResponse> getAll(){
        return facultyUtil.listEntitiesToResponse(facultyRepository.findAll());
    }

    public FacultyResponse save(CreateFacultyRequest request){
        Faculty entity = facultyUtil.createRequestToEntity(request);
        return facultyUtil.entityToResponse(facultyRepository.save(entity));
    }

    @Transactional
    public FacultyResponse update(UpdateFacultyRequest request, UUID id){
        Faculty findToUpdate = facultyRepository.findById(id).orElseThrow(() -> new NotFoundException(FACULTY_EXCEPTION.FACULTY_NOT_FOUND));
        facultyUtil.updateRequestToEntity(request, findToUpdate);
        findToUpdate = facultyRepository.save(findToUpdate);
        return facultyUtil.entityToResponse(findToUpdate);
    }

    public void delete(UUID id){
        if(!facultyRepository.existsById(id))
            throw new NotFoundException(FACULTY_EXCEPTION.FACULTY_NOT_FOUND);
        facultyRepository.deleteById(id);
    }
}