package com.example.json_excel_csv_fileupload.controller;

import com.example.json_excel_csv_fileupload.User;
import com.example.json_excel_csv_fileupload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FileController {
    @Autowired private FileService fileService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("user", new User());
        List<User> users = fileService.findAll();
        model.addAttribute("users", users);
        return "view/users";
    }

    @PostMapping("/fileupload")
    public String uploadFile(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        System.out.println("in here");
        boolean isFlag = fileService.saveDataFromUploadFile(user.getFile());
        System.out.println(isFlag);
        if(isFlag){
            redirectAttributes.addFlashAttribute("successmessage", "File upload successful.");
        }else{
            redirectAttributes.addFlashAttribute("errormessage", "File upload not successful," +
                    "please try again.");
        }

        return "redirect:/";
    }
}
