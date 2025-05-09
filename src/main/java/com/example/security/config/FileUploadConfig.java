package com.example.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.File;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${spring.servlet.multipart.max-file-size:10MB}")
    private String maxFileSize;

    @Value("${app.file.upload-dir:uploads/etudiants}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        // Création du répertoire s'il n'existe pas
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Répertoire de téléchargement créé: " + directory.getAbsolutePath());
            } else {
                System.out.println("ERREUR: Impossible de créer le répertoire: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Répertoire de téléchargement existe déjà: " + directory.getAbsolutePath());
        }

        // Vérification des permissions
        if (!directory.canWrite()) {
            System.out.println("ATTENTION: Le répertoire n'a pas les permissions d'écriture: " + directory.getAbsolutePath());
        } else {
            System.out.println("Permissions d'écriture OK pour: " + directory.getAbsolutePath());
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuration du gestionnaire de ressources pour servir les fichiers téléchargés
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");

        System.out.println("Gestionnaire de ressources configuré pour le répertoire: " + uploadDir);
    }
}