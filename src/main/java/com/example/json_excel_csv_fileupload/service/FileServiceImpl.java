package com.example.json_excel_csv_fileupload.service;

import com.example.json_excel_csv_fileupload.User;
import com.example.json_excel_csv_fileupload.repository.FileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class FileServiceImpl implements FileService{
    @Autowired private FileRepository fileRepository;

    @Override
    public List<User> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public boolean saveDataFromUploadFile(MultipartFile file) {
        boolean isFlag = false;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(extension.equalsIgnoreCase("json")){
            isFlag = readDataFromJson(file, extension);
        } else if(extension.equalsIgnoreCase("csv")){
            isFlag = readDataFromCSV(file, extension);
        } else if(extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")){
        isFlag = readDataFromExcel(file, extension);
        }

        return isFlag;
    }

    private boolean readDataFromJson(MultipartFile file, String extension) {
        try{
            InputStream inputStream = file.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
//            List<User> users = Arrays.asList(mapper.readValue(inputStream, User.class));
            List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});

            if(users != null && !users.isEmpty()){
                for(User each: users){
                    each.setFileType(extension);
                    fileRepository.save(each);
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean readDataFromCSV(MultipartFile file, String extension) {
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader =  new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> rows = csvReader.readAll();
            for(String[] row: rows){
                fileRepository.save(new User(row[0], row[1], row[2], row[3], extension));
            }
            return true;
        } catch (IOException e) {
            return false;
        } catch (CsvException e) {
//            throw new RuntimeException(e);
            return false;
        }

    }


    private boolean readDataFromExcel(MultipartFile file, String extension) {
        Workbook workbook = getWorkBook(file, extension);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        while(rows.hasNext()){
            Row row =  rows.next();
            User user =  new User();
            if(row.getCell(0).getCellType() == CellType.STRING){
                user.setFirstName(row.getCell(0).getStringCellValue());
            }
            if(row.getCell(1).getCellType() == CellType.STRING){
                user.setFirstName(row.getCell(1).getStringCellValue());
            }
            if(row.getCell(2).getCellType() == CellType.STRING){
                user.setFirstName(row.getCell(2).getStringCellValue());
            }
            if(row.getCell(3).getCellType() == CellType.NUMERIC){
                String phoneNumber = NumberToTextConverter.toText(row.getCell(3).getNumericCellValue());
                user.setPhoneNumber(phoneNumber);
            }else if (row.getCell(3).getCellType() == CellType.STRING){
                user.setPhoneNumber(row.getCell(3).getStringCellValue());
            }

            user.setFileType(extension);
            fileRepository.save(user);
        }
        return true;
    }

    private Workbook getWorkBook(MultipartFile file, String extension) {
        Workbook workbook = null;
        try{
            if(extension.equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(file.getInputStream());

            }else if(extension.equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(file.getInputStream());

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return workbook;
    }

}
