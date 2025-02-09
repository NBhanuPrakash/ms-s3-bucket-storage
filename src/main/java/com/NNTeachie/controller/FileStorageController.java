package com.NNTeachie.controller;

import com.NNTeachie.service.FileStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class FileStorageController {

    @Autowired
    private FileStorageServiceImpl service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.fileuplod(file));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<ByteArrayResource> dowmload(@PathVariable String filename) {
        byte[] bytes = service.downloadFIle(filename);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachement: filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
        service.deleteFile(filename);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all/files")
    public ResponseEntity<List<String>> getAllFiles() {
        return ResponseEntity.ok().body(service.listOfAllFiles());
    }
}
