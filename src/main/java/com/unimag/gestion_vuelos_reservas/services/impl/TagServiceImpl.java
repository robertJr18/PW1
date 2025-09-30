package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import com.unimag.gestion_vuelos_reservas.repositories.TagRepository;
import com.unimag.gestion_vuelos_reservas.services.TagService;
import com.unimag.gestion_vuelos_reservas.services.mapper.TagMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public TagDtos.TagResponse createTag(TagDtos.TagCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("TagCreateRequest cannot be null");
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be null or blank");
        }

        var tag = TagMapper.toEntity(request);
        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    @Transactional(readOnly = true)
    public TagDtos.TagResponse getTag(Long id) {
        return tagRepository.findById(id).map(TagMapper::toResponse).orElseThrow(() -> new NotFoundException("Tag % not found".formatted(id)));
    }

    @Override
    public void deleteTag(@Nonnull Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDtos.TagResponse> listAllTags() {
        List<TagDtos.TagResponse> tags = tagRepository.findAll().stream()
                .map(TagMapper::toResponse)
                .toList();

        if (tags.isEmpty()) {
            throw new NotFoundException("No tags found");
        }

        return tags;  }

    @Override
    @Transactional(readOnly = true)
    public List<TagDtos.TagResponse> listTagsByNameIn(List<String> names) {
        if (names == null || names.isEmpty()) {
            throw new IllegalArgumentException("Tag names list cannot be null or empty");
        }

        List<TagDtos.TagResponse> tags = tagRepository.findByNameIn(names).stream()
                .map(TagMapper::toResponse)
                .toList();

        if (tags.isEmpty()) {
            throw new NotFoundException("No tags found for given names: " + names);
        }

        return tags; }
}
