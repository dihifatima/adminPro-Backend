package com.example.security.service.facade;
import java.io.IOException;
public interface PdfService {

    byte[] generateRendezVousBilan(Long demandeServiceId) throws IOException;
}