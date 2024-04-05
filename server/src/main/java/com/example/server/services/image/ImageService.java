package com.example.server.services.image;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public interface ImageService {
    public String convertImage(Blob blob) throws IOException, SQLException;
}
