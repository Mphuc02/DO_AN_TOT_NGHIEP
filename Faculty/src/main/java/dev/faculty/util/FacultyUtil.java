package dev.faculty.util;

import dev.common.dto.response.FacultyResponse;
import dev.faculty.dto.request.CreateFacultyRequest;
import dev.faculty.dto.request.UpdateFacultyRequest;
import dev.faculty.entity.Faculty;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacultyUtil {
    public Faculty createRequestToEntity(CreateFacultyRequest request){
        return Faculty.builder()
                .name(request.getName())
                .facultyDescribe(request.getDescribe())
                .build();
    }

    public void updateRequestToEntity(UpdateFacultyRequest request, Faculty entityToUpdate){
        if(!ObjectUtils.isEmpty(request.getName()))
            entityToUpdate.setName(request.getName());

        if(!ObjectUtils.isEmpty(request.getDescribe()))
            entityToUpdate.setFacultyDescribe(request.getDescribe());
    }

    public FacultyResponse entityToResponse(Faculty entity){
        return FacultyResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .describe(entity.getFacultyDescribe())
                .build();
    }

    public List<FacultyResponse> listEntitiesToResponse(List<Faculty> faculties){
        return faculties.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}