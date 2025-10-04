package com.example.memories.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3;
    private final String bucketName;

    public S3Service(@Value("${aws.accessKey}") String accessKey,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.bucket}") String bucket,
                     @Value("${aws.region}") String region) {
        this.bucketName = bucket;
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                ).build();
    }

    public String uploadFile(org.springframework.web.multipart.MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        String key = System.currentTimeMillis() + "_" + file.getName();

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                file.toPath());

        file.delete();
        return "https://" + bucketName + ".s3." + s3.region().id() + ".amazonaws.com/" + key;
    }

    private File convertMultiPartToFile(org.springframework.web.multipart.MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
