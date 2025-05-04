package com.example.security.ws.facade;

import com.example.security.entity.ServiceOffert;
import com.example.security.entity.TypeService;
import com.example.security.service.facade.ServiceOffertService;
import com.example.security.ws.converter.ServiceOffertConverter;
import com.example.security.ws.dto.ServiceOffertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceOffertController {

    private final ServiceOffertService serviceOffertService;
    private final ServiceOffertConverter serviceConverter;

    @Autowired
    public ServiceOffertController(ServiceOffertService serviceService, ServiceOffertConverter serviceConverter) {
        this.serviceOffertService = serviceService;
        this.serviceConverter = serviceConverter;
    }

    @PostMapping("")
    public ResponseEntity<ServiceOffertDto> create(@RequestBody ServiceOffertDto serviceDto) {
        ServiceOffert service = serviceConverter.map(serviceDto);
        ServiceOffert savedService = serviceOffertService.save(service);
        return new ResponseEntity<>(serviceConverter.map(savedService), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<ServiceOffertDto> update(@RequestBody ServiceOffertDto serviceDto) {
        ServiceOffert service = serviceConverter.map(serviceDto);
        ServiceOffert updatedService = serviceOffertService.update(service);
        if (updatedService == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serviceConverter.map(updatedService)); // <-- تصحيح هنا
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int result = serviceOffertService.delete(id);
        if (result < 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffertDto> findById(@PathVariable Long id) {
        ServiceOffert service = serviceOffertService.findById(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serviceConverter.map(service));
    }

    @GetMapping("")
    public ResponseEntity<List<ServiceOffertDto>> findAll() {
        List<ServiceOffert> services = serviceOffertService.findAll();
        return ResponseEntity.ok(serviceConverter.mapListEntities(services));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ServiceOffertDto>> findDisponibles() {
        List<ServiceOffert> services = serviceOffertService.findByEstDisponible(true);
        return ResponseEntity.ok(serviceConverter.mapListEntities(services));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ServiceOffertDto>> findByType(@PathVariable String type) {
        try {
            TypeService typeService = TypeService.valueOf(type);
            List<ServiceOffert> services = serviceOffertService.findByType(typeService);
            return ResponseEntity.ok(serviceConverter.mapListEntities(services));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<ServiceOffertDto>> rechercherServices(@RequestParam String nom) {
        List<ServiceOffert> services = serviceOffertService.findByNomContainingIgnoreCase(nom);
        return ResponseEntity.ok(serviceConverter.mapListEntities(services));
    }



    @GetMapping("/type/{type}/disponibles")
    public ResponseEntity<List<ServiceOffertDto>> findServicesDiponiblesByType(@PathVariable String type) {
        try {
            TypeService typeService = TypeService.valueOf(type);
            List<ServiceOffert> services = serviceOffertService.findByTypeAndEstDisponibleTrue(typeService);
            return ResponseEntity.ok(serviceConverter.mapListEntities(services));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
