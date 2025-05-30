package com.example.security.ws.facade;

import com.example.security.service.facade.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "*")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generer-rendezvous/{demandeServiceId}")
    public ResponseEntity<byte[]> genererPdfRendezVous(@PathVariable Long demandeServiceId) {
        try {
            // Validation de l'ID
            if (demandeServiceId == null || demandeServiceId <= 0) {
                System.err.println("ID de demande service invalide: " + demandeServiceId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID de demande service invalide".getBytes());
            }

            // Log pour débuggage
            System.out.println("=== DEBUG PDF GENERATION ===");
            System.out.println("Demande Service ID: " + demandeServiceId);
            System.out.println("============================");

            // Génération du PDF
            byte[] pdfBytes = pdfService.generateRendezVousBilan(demandeServiceId);

            if (pdfBytes == null || pdfBytes.length == 0) {
                System.err.println("PDF généré est vide");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de la génération du PDF".getBytes());
            }

            // Configuration des headers pour le téléchargement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Nom du fichier avec ID et timestamp
            String fileName = String.format("rendezvous_%d_%s.pdf",
                    demandeServiceId,
                    LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            );

            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);

            System.out.println("PDF généré avec succès: " + fileName + " (" + pdfBytes.length + " bytes)");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur validation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("Demande service introuvable: " + e.getMessage()).getBytes());
        } catch (IOException e) {
            System.err.println("Erreur IOException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la génération du PDF: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            System.err.println("Erreur générale: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur interne: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/previsualiser-rendezvous/{demandeServiceId}")
    public ResponseEntity<byte[]> previsualiserPdfRendezVous(@PathVariable Long demandeServiceId) {
        try {
            // Validation de l'ID
            if (demandeServiceId == null || demandeServiceId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID de demande service invalide".getBytes());
            }

            // Génération du PDF pour prévisualisation
            byte[] pdfBytes = pdfService.generateRendezVousBilan(demandeServiceId);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de la génération du PDF".getBytes());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);

            // Pour la prévisualisation, affichage inline
            headers.setContentDispositionFormData("inline", "preview.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur validation prévisualisation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("Demande service introuvable: " + e.getMessage()).getBytes());
        } catch (IOException e) {
            System.err.println("Erreur IOException prévisualisation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la génération du PDF: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            System.err.println("Erreur générale prévisualisation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur interne: " + e.getMessage()).getBytes());
        }
    }
}