package dev.workingschedule.util;

import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.workingschedule.dto.request.SaveWorkingScheduleRequest;
import dev.workingschedule.entity.WorkingSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkingScheduleMapperUtil {
    WorkingSchedule mapCreateRequestToEntity(SaveWorkingScheduleRequest request);

    void mapUpdateRequestToEntity(SaveWorkingScheduleRequest request, @MappingTarget WorkingSchedule entity);

    WorkingScheduleCommonResponse mapEntityToCommonResponse(WorkingSchedule entity);

    List<WorkingScheduleCommonResponse> mapEntitiesToCommonResponses(List<WorkingSchedule> schedules);
}