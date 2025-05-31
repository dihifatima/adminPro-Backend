package com.example.security.service.impl;

import com.example.security.entity.DemandeService;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.service.facade.PdfService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PdfServiceImpl implements PdfService {

    private final DemandeServiceRepository demandeServiceRepository;
    private final TemplateEngine templateEngine;

    public PdfServiceImpl(DemandeServiceRepository demandeServiceRepository, TemplateEngine templateEngine) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] generateRendezVousBilan(Long demandeServiceId) throws IOException {
        // Récupération de la demande de service avec toutes ses relations
        Optional<DemandeService> demandeOpt = demandeServiceRepository.findById(demandeServiceId);

        if (demandeOpt.isEmpty()) {
            throw new IllegalArgumentException("Demande de service introuvable avec l'ID: " + demandeServiceId);
        }

        DemandeService demande = demandeOpt.get();

        // Validation des données essentielles
        if (demande.getUser() == null) {
            throw new IllegalArgumentException("Utilisateur manquant pour la demande: " + demandeServiceId);
        }

        if (demande.getServiceOffert() == null) {
            throw new IllegalArgumentException("Service offert manquant pour la demande: " + demandeServiceId);
        }

        // Préparation du contexte Thymeleaf
        Context context = createThymeleafContext(demande);

        // Génération du HTML
        String htmlContent = templateEngine.process("bilan_rendezvous", context);

        // Conversion HTML vers PDF avec OpenHTMLtoPDF
        return convertHtmlToPdf(htmlContent);
    }

    private Context createThymeleafContext(DemandeService demande) {
        Context context = new Context();

        // Informations de base
        context.setVariable("ref", demande.getRef());

        // Informations utilisateur
        if (demande.getUser() != null) {
            context.setVariable("userNom",
                    demande.getUser().getFirstname() + " " + demande.getUser().getLastname());
            context.setVariable("email", demande.getUser().getEmail());
            context.setVariable("telephone",
                    demande.getUser().getTelephone() != null ? demande.getUser().getTelephone() : "Non renseigné");
        }

        // Informations service
        if (demande.getServiceOffert() != null) {
            context.setVariable("serviceOffertNom", demande.getServiceOffert().getName());
        }

        // Informations créneau
        if (demande.getCreneau() != null) {
            String creneauInfo = String.format("%s de %s à %s",
                    demande.getCreneau().getDateCreneau(),
                    demande.getCreneau().getHeureDebut(),
                    demande.getCreneau().getHeureFin());
            context.setVariable("creneau", creneauInfo);
        } else {
            context.setVariable("creneau", "Non défini");
        }

        // Formatage des dates
        if (demande.getDateSoumission() != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            context.setVariable("dateSoumission",
                    demande.getDateSoumission().format(dateTimeFormatter));
            context.setVariable("date",
                    demande.getDateSoumission().format(dateFormatter));
        }

        // Date de génération
        LocalDateTime now = LocalDateTime.now();
        context.setVariable("generationDateTime",
                now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("generationDate",
                now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return context;
    }

    private byte[] convertHtmlToPdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Erreur lors de la génération du PDF depuis le HTML : " + e.getMessage(), e);
        }
    }
}