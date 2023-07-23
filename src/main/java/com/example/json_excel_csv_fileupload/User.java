package com.example.json_excel_csv_fileupload;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String fileType;
    @Transient
    private MultipartFile file;

    public User(String firstName, String lastName, String email, String phoneNumber, String fileType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fileType = fileType;
    }

    public User() {

    }
}
