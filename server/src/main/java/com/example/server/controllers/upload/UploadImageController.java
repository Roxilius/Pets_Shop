package com.example.server.controllers.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.services.Upload.UploadImageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("upload")
@RestController
@Slf4j
@Tag(name = "user")
public class UploadImageController {
    @Autowired
    UploadImageService uploadImageService;
    @PostMapping(value="/upload-user-image",
    consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadUserImage(@RequestParam String User, @RequestParam("UserImage") MultipartFile file){
        try{
            uploadImageService.uploadUserImage(User, file);
            return ResponseEntity.ok().body(GenericResponse.success(null,"Successfuly Upload Image"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Image Upload Failed"));
        }
    }
}