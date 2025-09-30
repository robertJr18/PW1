package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.services.mapper.TagMapper;

import java.util.Collection;
import java.util.List;

public interface TagService {
    TagDtos.TagResponse createTag(TagDtos.TagCreateRequest request);
    TagDtos.TagResponse getTag(Long id);
    void deleteTag(Long id);
    List<TagDtos.TagResponse> listAllTags();
    List<TagDtos.TagResponse> listTagsByNameIn(List<String> names);
}
