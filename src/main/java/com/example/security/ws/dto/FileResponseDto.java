package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponseDto {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
