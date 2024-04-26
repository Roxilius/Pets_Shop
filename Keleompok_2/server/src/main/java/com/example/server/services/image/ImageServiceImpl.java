package com.example.server.services.image;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService{
    @Override
    public String convertImage(Blob blob) throws IOException, SQLException {
        byte[] blobBytes = blob.getBytes(1, (int) blob.length());
        return Base64.getEncoder().encodeToString(blobBytes);
    }
}
