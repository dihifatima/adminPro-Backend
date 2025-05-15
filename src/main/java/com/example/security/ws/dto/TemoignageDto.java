package com.example.security.ws.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TemoignageDto {
    private Long id;
    private String nom;
    private String ville;
    private String typeService;
    @Column(length = 1500)
    private String description;
    private LocalDateTime dateSoumission;
    private Long nbrDeStart ;
}
