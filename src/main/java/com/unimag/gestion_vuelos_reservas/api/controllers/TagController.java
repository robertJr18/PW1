package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.TagDtos;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import com.unimag.gestion_vuelos_reservas.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagController {
    private final TagService service;

    @PostMapping
    public ResponseEntity<TagDtos.TagResponse> create(
            @Valid @RequestBody TagDtos.TagCreateRequest request, UriComponentsBuilder uriBuilder) {

        var body = service.createTag(request);
        var location = uriBuilder.path("/api/tags/{id}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDtos.TagResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTag(id));
    }

    @GetMapping
    public ResponseEntity<List<TagDtos.TagResponse>> listAll() {
        return ResponseEntity.ok(service.listAllTags());
    }

    @GetMapping("/by-names")
    public ResponseEntity<List<TagDtos.TagResponse>> listByNames(@RequestParam List<String> names) {
        return ResponseEntity.ok(service.listTagsByNameIn(names));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteTag(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
