package com.example.filmography.rest.controller;

import com.example.filmography.dto.GenericDto;
import com.example.filmography.mapper.GenericMapper;
import com.example.filmography.model.GenericModel;
import com.example.filmography.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class GenericController <T extends GenericModel, N extends GenericDto>{

    private final GenericService<T> service;
    private final GenericMapper<T, N> mapper;
    protected GenericController(GenericService<T> service, GenericMapper<T, N> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(description = "Получить список всех записей", method = "GetAll")
    @GetMapping("/list")
    public ResponseEntity<List<N>> getAll() {
        return ResponseEntity.ok().body(service.listAll().stream().map(mapper::toDto).toList());
    }

    @Operation(description = "Получить запись по id", method = "GetOne")
    @GetMapping("/{id}")
    public ResponseEntity<N> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(service.getOne(id)));
    }

    @Operation(description = "Создать запись", method = "Create")
    @PostMapping
    public ResponseEntity<N> create(@RequestBody N object) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(service.create(mapper.toEntity(object))));
    }

    @Operation(description = "Обновить запись", method = "Update")
    @PutMapping("/{id}")
    public ResponseEntity<N> update(@RequestBody N object, @PathVariable Long id) {
        object.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(service.update(mapper.toEntity(object))));
    }

    @Operation(description = "Удалить запись", method = "Delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
