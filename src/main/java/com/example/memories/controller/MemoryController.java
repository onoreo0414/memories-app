package com.example.memories.controller;

import com.example.memories.model.Memory;
import com.example.memories.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${memories.upload-dir:uploads}")
    private String uploadDir;

    public MemoryController(MemoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Memory> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Memory create(
            @RequestParam("date") String date,
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("author") String author,
            @RequestParam("photo") MultipartFile photo
    ) throws IOException {
        // ファイル保存
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
        File dest = new File(dir, filename);
        photo.transferTo(dest);

        Memory memory = new Memory();
        memory.setDate(LocalDate.parse(date));
        memory.setTitle(title);
        memory.setMessage(message);
        memory.setAuthor(author);
        memory.setPhotoFilename(filename);

        return repository.save(memory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
