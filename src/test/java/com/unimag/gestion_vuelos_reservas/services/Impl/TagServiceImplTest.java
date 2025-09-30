package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import com.unimag.gestion_vuelos_reservas.repositories.TagRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    // --- CREATE ---
    @Test
    void shouldcreateTag() {
        var request = new TagDtos.TagCreateRequest("promo");
        var savedTag = Tag.builder().id(1L).name("promo").build();
        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);

        var response = tagService.createTag(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("promo");

        verify(tagRepository).save(any(Tag.class));
    }

    // --- GET ---
    @Test
    void shouldgetTag() {
        var tag = Tag.builder().id(1L).name("eco").build();
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        var response = tagService.getTag(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("eco");
    }

    // --- DELETE ---
    @Test
    void shouldDeleteTag() {
        var tag= Tag.builder().id(2L).name("eco").build();
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        tagService.deleteTag(2L);

        verify(tagRepository).delete(tag);
    }

    // --- LIST ALL ---
    @Test
    void listAllTags_success() {
        var tags = List.of(Tag.builder().id(1L).name("eco").build());
        when(tagRepository.findAll()).thenReturn(tags);

        var response = tagService.listAllTags();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).name()).isEqualTo("eco");
    }

    // --- LIST BY NAME ---
    @Test
    void listTagsByNameIn_success() {
        var names = List.of("eco", "promo");
        var tags = List.of(Tag.builder().id(1L).name("eco").build());
        when(tagRepository.findByNameIn(names)).thenReturn(tags);

        var response = tagService.listTagsByNameIn(names);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).name()).isEqualTo("eco");
    }
}

