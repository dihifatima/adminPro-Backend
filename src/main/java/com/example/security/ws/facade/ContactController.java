package com.example.security.ws.facade;

import com.example.security.entity.Contact;
import com.example.security.service.facade.ContactService;
import com.example.security.ws.converter.ContactConverter;
import com.example.security.ws.dto.ContactDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;
    private final ContactConverter contactConverter;

    public ContactController(ContactService contactService, ContactConverter contactConverter) {
        this.contactService = contactService;
        this.contactConverter = contactConverter;
    }

    @PostMapping
    public ContactDto ajouterContact(@RequestBody ContactDto contactDto) {
        Contact contact = contactConverter.map(contactDto);
        Contact saved = contactService.ajouterContact(contact);
        return contactConverter.map(saved);
    }

    @GetMapping
    public List<ContactDto> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return contactConverter.mapListEntities(contacts);
    }
}
