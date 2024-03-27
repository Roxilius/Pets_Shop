package com.example.server.services.Upload;

import java.io.IOException;
import java.sql.SQLException;

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

    @SuppressWarnings("null")
	@Override
    public void uploadUserImage(String userId, MultipartFile userImage) throws IOException, SQLException {
        if (!userImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setImage(new SerialBlob(userImage.getBytes()));
            usersRepository.saveAndFlush(user);
        }
    }

    @Override
    public void uploadProductImage(String productId, MultipartFile ProductImage) throws IOException, SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'uploadProductImage'");
    }
}
