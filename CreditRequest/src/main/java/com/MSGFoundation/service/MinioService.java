package com.MSGFoundation.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MinioService {

    @Value("${minio.url.prd}")
    private String minioUrl;
    
    @Value("${minio.url}")
    private String minioUrlMinio;
   
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
    
    public String generarPresignedUrl(String bucketName, String objectName) {
        try {
            MinioClient minio_Client = MinioClient.builder()
                .endpoint(minioUrlMinio)
                .credentials("U0Xy75HOBtIwZc1H", "quBrs23RZ4BMPbozd16Qbrw3IVbgERuK")
                .build();

            return minio_Client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(60 * 60) // URL válida por 1 hora
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al generar URL pre-firmada", e);
        }
    }
}
