package com.example.test_pt_sofco_graha_gaji.id.service;

import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
@DependsOn("firebaseApp")
@RequiredArgsConstructor
public class FirebaseStorageService {
        private final FirebaseApp firebaseApp;

        public String uploadImage(MultipartFile file) throws IOException {
                Bucket bucket = StorageClient.getInstance(firebaseApp)
                                .bucket("fir-novendra-portfolio.firebasestorage.app");

                String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo.jpg";

                // TAMBAHKAN prefix 'gaji.id/' di sini
                String fileName = String.format("gaji.id/attendance/%s-%s",
                                UUID.randomUUID().toString(),
                                originalFilename);

                String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

                bucket.create(fileName, file.getBytes(), contentType);

                // Return URL tetap sama kodenya, replace("/") akan menangani semua slash
                // menjadi %2F
                return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                                bucket.getName(),
                                fileName.replace("/", "%2F"));
        }
}