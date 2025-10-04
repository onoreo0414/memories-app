// src/main/java/com/example/memories/controller/MemoryController.java
package com.example.memories.controller;

import com.example.memories.model.Memory;
import com.example.memories.repository.MemoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memories")
@CrossOrigin
public class MemoryController {

    private final MemoryRepository memoryRepository;

    public MemoryController(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @GetMapping
    public List<Memory> getAll() {
        return memoryRepository.findAll();
    }

    @PostMapping
    public Memory create(@RequestBody Memory memory) {
        return memoryRepository.save(memory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memoryRepository.deleteById(id);
    }
}
