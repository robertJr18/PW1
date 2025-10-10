package com.unimag.gestion_vuelos_reservas.api.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.services.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TagController.class)
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagService tagService;

    @Test
    void create_shouldReturn201() throws Exception {
        var request = new TagDtos.TagCreateRequest("Direct Flight");
        var response = new TagDtos.TagResponse(1L, "Direct Flight", List.of());

        when(tagService.createTag(any(TagDtos.TagCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/tags/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Direct Flight"));

        verify(tagService).createTag(any(TagDtos.TagCreateRequest.class));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var response = new TagDtos.TagResponse(1L, "Direct Flight", List.of());

        when(tagService.getTag(1L)).thenReturn(response);

        mockMvc.perform(get("/api/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Direct Flight"));

        verify(tagService).getTag(1L);
    }

    @Test
    void get_shouldReturn404() throws Exception {
        when(tagService.getTag(999L)).thenThrow(new NotFoundException("Tag not found"));

        mockMvc.perform(get("/api/tags/999"))
                .andExpect(status().isNotFound());

        verify(tagService).getTag(999L);
    }

    @Test
    void listAll_shouldReturn200() throws Exception {
        var tag1 = new TagDtos.TagResponse(1L, "Direct Flight", List.of());
        var tag2 = new TagDtos.TagResponse(2L, "Economy", List.of());
        var tags = List.of(tag1, tag2);

        when(tagService.listAllTags()).thenReturn(tags);

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Direct Flight"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Economy"));

        verify(tagService).listAllTags();
    }

    @Test
    void listByNames_shouldReturn200() throws Exception {
        var tag1 = new TagDtos.TagResponse(1L, "Direct Flight", List.of());
        var tag2 = new TagDtos.TagResponse(2L, "Economy", List.of());
        var tags = List.of(tag1, tag2);

        when(tagService.listTagsByNameIn(List.of("Direct Flight", "Economy"))).thenReturn(tags);

        mockMvc.perform(get("/api/tags/by-names")
                        .param("names", "Direct Flight", "Economy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Direct Flight"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Economy"));

        verify(tagService).listTagsByNameIn(List.of("Direct Flight", "Economy"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/tags/1"))
                .andExpect(status().isNoContent());

        verify(tagService).deleteTag(1L);
    }
}
