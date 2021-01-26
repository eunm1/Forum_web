package com.eunm1.forum.dto;

import com.eunm1.forum.db.entity.Article;
import com.eunm1.forum.db.entity.File;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String orignFilename;
    private String fileName;
    private String filePath;

    public File toEntity(){
        File build = File.builder()
                .id(id)
                .orignFilename(orignFilename)
                .filename(fileName)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public FileDto(Long id, String orignFilename, String fileName, String filePath){
        this.id = id;
        this.orignFilename = orignFilename;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
