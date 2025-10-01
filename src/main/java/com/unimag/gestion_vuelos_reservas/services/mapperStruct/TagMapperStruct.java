package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Mapper(
        componentModel = "spring",
        uses = {FlightMapperStruct.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TagMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Tag toEntity(TagDtos.TagCreateRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    void updateEntity(@MappingTarget Tag tag, TagDtos.TagUpdateRequest request);

    @Mapping(target = "flights", source = "flights")
    TagDtos.TagResponse toResponse(Tag tag);

}
