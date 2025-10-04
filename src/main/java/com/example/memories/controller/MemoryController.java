package com.example.memories.controller;

import com.example.memories.model.Memory;
import com.example.memories.repository.MemoryRepository;
import com.example.memories.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    @Autowired
    private MemoryRepository repository;

    @Autowired
    private S3Service s3Service;

    private final String bucket = System.getenv("S3_BUCKET");

    @GetMapping
    public List<Memory> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Memory create(@RequestParam String author,
                         @RequestParam String title,
                         @RequestParam String message,
                         @RequestParam String date,
                         @RequestParam MultipartFile photo) throws IOException {

        // 一時的にローカル保存
        File tmpFile = File.createTempFile("upload-", photo.getOriginalFilename());
        photo.transferTo(tmpFile);

        // S3にアップロード
        String s3Url = s3Service.uploadFile(bucket, photo.getOriginalFilename(), tmpFile.getAbsolutePath());

        Memory memory = new Memory();
        memory.setAuthor(author);
        memory.setTitle(title);
        memory.setMessage(message);
        memory.setDate(LocalDate.parse(date));
        memory.setPhotoUrl(s3Url);

        return repository.save(memory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
