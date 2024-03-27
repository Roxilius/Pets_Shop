package com.example.server.services.Upload;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.models.Users;
import com.example.server.repositorys.UsersRepository;

@Service
public class UploadImageServiceImpl implements UploadImageService{
    @Autowired
    UsersRepository usersRepository;
    @Override
    public void uploadUserImage(String UserId, MultipartFile userImage) throws IOException, SQLException {
        String[] fileName = Objects.requireNonNull(userImage.getOriginalFilename()).split(".");
        if (!fileName[fileName.length -1].equalsIgnoreCase("jpg") &&
            !fileName[fileName.length - 1].equalsIgnoreCase("jpeg") &&
            !fileName[fileName.length - 1].equalsIgnoreCase("png")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsopperted File Type");
        }
        @SuppressWarnings("null")
        Users user = usersRepository.findById(UserId).orElse(null);
        if (user != null) {
            user.setImage(new SerialBlob(userImage.getBytes()));
            usersRepository.saveAndFlush(user);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found");
        }
    }

    @Override
    public void uploadProductImage(String productId, MultipartFile ProductImage) throws IOException, SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'uploadProductImage'");
    }
    
}
