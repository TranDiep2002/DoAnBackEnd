package com.example.doan2.until;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

@Component
public class FileDownloadUntil {
    private Path foundFile;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Resource getFileAsResource(Integer maDeTai) throws IOException {
        Path dirPath = Paths.get(uploadDir,"DeTai"+maDeTai);
        Files.list(dirPath).forEach(file -> {
            try {
                if (Files.list(Paths.get(String.valueOf(dirPath))).anyMatch(Files::isRegularFile)) {
                    foundFile = file;
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}