package com.example.memories.controller;

import com.example.memories.model.Memory;
import com.example.memories.repository.MemoryRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
@CrossOrigin
public class MemoryController {

    private final MemoryRepository repository;
    private final String uploadDir = "uploads/";

    public MemoryController(MemoryRepository repository) {
        this.repository = repository;
        new File(uploadDir).mkdirs(); // アップロードフォルダ作成
    }

    @GetMapping
    public List<Memory> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Memory create(@RequestParam String title,
                         @RequestParam String message,
                         @RequestParam String author,
                         @RequestParam String date,
                         @RequestParam(required = false) MultipartFile photo) throws IOException {

        Memory memory = new Memory();
        memory.setTitle(title);
        memory.setMessage(message);
        memory.setAuthor(author);
        memory.setDate(LocalDate.parse(date));

        if(photo != null && !photo.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            photo.transferTo(dest);
            memory.setPhotoUrl("/uploads/" + fileName);
        }

        return repository.save(memory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
