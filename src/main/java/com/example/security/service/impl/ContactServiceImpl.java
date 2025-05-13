package com.example.security.service.impl;

import com.example.security.dao.ContactRepository;
import com.example.security.email2.EmailService2;
import com.example.security.entity.Contact;
import com.example.security.service.facade.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final EmailService2 emailService; // Service pour l'envoi d'e-mail

    public ContactServiceImpl(ContactRepository contactRepository, EmailService2 emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }

    @Override
    public Contact ajouterContact(Contact contact) {
        // Enregistrer le contact dans la base de données
        Contact savedContact = contactRepository.save(contact);

        // Envoi de l'email après l'ajout du contact
        emailService.sendEmail(savedContact.getNom(), savedContact.getEmail(), savedContact.getSujet());

        // Retourner le contact enregistré
        return savedContact;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
}