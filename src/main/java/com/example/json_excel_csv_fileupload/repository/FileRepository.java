package com.example.json_excel_csv_fileupload.repository;

import com.example.json_excel_csv_fileupload.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<User, Long> {
}
