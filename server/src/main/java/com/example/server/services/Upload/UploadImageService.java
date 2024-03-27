package com.example.server.services.Upload;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {
    void uploadUserImage(String UserId,MultipartFile userImage)
    throws IOException, SQLException;

    void uploadProductImage(String productId,MultipartFile productImage)
    throws IOException, SQLException;
}
