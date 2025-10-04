package com.example.memories.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3;

    public S3Service() {
        this.s3 = S3Client.builder()
                .region(Region.AP_NORTHEAST_1) // 東京リージョン
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                    System.getenv("AWS_ACCESS_KEY_ID"),
                                    System.getenv("AWS_SECRET_ACCESS_KEY")
                                )
                        )
                )
                .build();
    }

    public String uploadFile(String bucketName, String keyName, String localPath) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3.putObject(request, Paths.get(localPath));
        return "https://" + bucketName + ".s3.amazonaws.com/" + keyName;
    }
}
