package com.MSGFTreasury.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MinioService {

    @Value("${minio.url.prd}")
    private String minioUrl;
   
    @Autowired
    private MinioClient minioClient;

    public String uploadFile(String fileName, InputStream inputStream, String contentType) throws Exception {
        try (InputStream stream = inputStream) { // try-with-resources para cerrar automáticamente
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("camundanzia")
                            .object(fileName)
                            .stream(stream, stream.available(), -1)
                            .contentType(contentType)
                            .build());
            return fileName;
        } catch (MinioException e) {
            throw new Exception("Error occurred while uploading the file to MinIO", e);
        }
    }
    
    public String getPreSignedUrl(String fileName) throws Exception {
        return String.format("%s/%s/%s", "https://" + this.minioUrl, "camundanzia", fileName);
    }
    public String getMinioUrl(){
        return minioUrl;
    }
}
