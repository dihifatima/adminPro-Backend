package com.example.security.service.facade;

import com.example.security.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact ajouterContact(Contact contact);
    List<Contact> getAllContacts();
}
