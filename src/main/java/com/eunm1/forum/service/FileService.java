package com.eunm1.forum.service;

import com.eunm1.forum.db.entity.Article;
import com.eunm1.forum.db.entity.File;
import com.eunm1.forum.db.repository.FileRepository;
import com.eunm1.forum.dto.ArticleDto;
import com.eunm1.forum.dto.FileDto;
import com.eunm1.forum.util.MD5Checksum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public Long saveFile(FileDto fileDto){
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id){
        File file = fileRepository.findById(id).get();
        //https://penpen.tistory.com/entry/Spring-Data-JPA-Repository-%EB%A9%94%EC%86%8C%EB%93%9C-%EC%BF%BC%EB%A6%AC-FK-%EB%A1%9C-findBy-%EB%A7%8C%EB%93%A4%EA%B8%B0

        FileDto fileDto = FileDto.builder()
                .id(file.getId())
                .orignFilename(file.getOrignFilename())
                .fileName(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }

    @Transactional
    public void deleteFile(Long id){

        String filePath = fileRepository.findById(id).get().getFilePath();
        java.io.File file = new java.io.File(filePath);

        if(file.exists()){
            file.delete();
        }

        fileRepository.deleteById(id);
    }

    @Transactional
    public FileDto FileCheck(MultipartFile file){
        FileDto fileDto = null;
        try{
            String origFileName = file.getOriginalFilename();
            String extention = "."+StringUtils.getFilenameExtension(origFileName);
            String fileName = new MD5Checksum(origFileName).toString()+extention;
            String savePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static"+"\\imgFiles";
            //String savePath = System.getProperty("user.dir")+"\\imgFiles";

            if(!new java.io.File(savePath).exists()){
                try{
                    new java.io.File(savePath).mkdir();
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\"+fileName;
            file.transferTo(new java.io.File(filePath));

            fileDto = FileDto.builder()
                    .orignFilename(origFileName)
                    .fileName(fileName)
                    .filePath(filePath)
                    .build();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return fileDto;
        }

    }

    public List<String> getFilenameList(List<ArticleDto> articleDtos){
        List<String> list = new ArrayList<>();

        for(ArticleDto article : articleDtos){
            File file = fileRepository.findById(article.getFileId()).get();
            list.add(file.getFilename());
        }
        return list;
    }
}
