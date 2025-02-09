package com.NNTeachie.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    String fileuplod(MultipartFile file);

    byte[] downloadFIle(String fileName);

    String deleteFile(String fileName);

    List<String> listOfAllFiles();
}
