package com.example.json_excel_csv_fileupload.service;

import com.example.json_excel_csv_fileupload.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<User> findAll();

    boolean saveDataFromUploadFile(MultipartFile file);
}
