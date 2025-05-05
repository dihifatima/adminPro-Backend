package com.example.security.ws.facade;

import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.ServiceOffertService;
import com.example.security.ws.converter.ServiceOffertConverter;
import com.example.security.ws.dto.ServiceOffertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceOffertController {


    @Autowired
    private ServiceOffertService serviceOffertService;

    @Autowired
    private ServiceOffertConverter serviceOffertConverter;

    // Créer un nouveau service offert
    @PostMapping
    public ResponseEntity<ServiceOffertDto> createServiceOffert(@RequestBody ServiceOffertDto serviceOffertDto) {
        ServiceOffert serviceOffert = serviceOffertConverter.map(serviceOffertDto);
        ServiceOffert savedServiceOffert = serviceOffertService.save(serviceOffert);
        return new ResponseEntity<>(serviceOffertConverter.map(savedServiceOffert), HttpStatus.CREATED);
    }

    // Mettre à jour les détails d'un service offert
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffertDto> updateServiceOffert(@PathVariable Long id, @RequestBody ServiceOffertDto serviceOffertDto) {
        ServiceOffert serviceOffert = serviceOffertConverter.map(serviceOffertDto);
        ServiceOffert updatedServiceOffert = serviceOffertService.updateServiceDetails(id, serviceOffert.getName());
        if (updatedServiceOffert != null) {
            return new ResponseEntity<>(serviceOffertConverter.map(updatedServiceOffert), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir tous les services offerts
    @GetMapping
    public ResponseEntity<List<ServiceOffertDto>> getAllServiceOfferts() {
        List<ServiceOffert> serviceOfferts = serviceOffertService.findAll();
        return new ResponseEntity<>(serviceOffertConverter.mapListEntities(serviceOfferts), HttpStatus.OK);
    }

    // Obtenir un service offert par son nom
    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceOffertDto> getServiceOffertByName(@PathVariable String name) {
        Optional<ServiceOffert> serviceOffertOpt = serviceOffertService.findByName(name);
        if (serviceOffertOpt.isPresent()) {
            return new ResponseEntity<>(serviceOffertConverter.map(serviceOffertOpt.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
