package com.NNTeachie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.bucket.name}")
    private String bucketname;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String fileuplod(MultipartFile file) {
        String originalFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            File file1 = convertMultiPartTiFile(file);
            PutObjectResult putObjectResult = s3Client.putObject(bucketname, originalFilename, file1);
            return "File Uploaded = " + putObjectResult.getContentMd5() + "_" + originalFilename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadFIle(String fileName) {
        S3Object object = s3Client.getObject(bucketname, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketname, fileName);
        return "File Deleted SuccessFully";
    }

    @Override
    public List<String> listOfAllFiles() {
        ObjectListing objectListing = s3Client.listObjects(bucketname);
        return objectListing.getObjectSummaries().stream().map(list -> list.getKey()).collect(Collectors.toList());
    }

    private File convertMultiPartTiFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertFile;
    }
}
