package com.example.security.ws.facade;

import com.example.security.ws.converter.BlogSectionConverter;
import com.example.security.ws.dto.BlogSectionDTO;
import com.example.security.entity.BlogSection;
import com.example.security.service.facade.BlogSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/blog-sections")
public class BlogSectionController {

    @Autowired
    private BlogSectionService service;

    @Autowired
    private BlogSectionConverter converter;

    @PostMapping("/")
    public BlogSectionDTO save(@RequestBody BlogSectionDTO dto) {
        BlogSection entity = converter.toEntity(dto);
        BlogSection saved = service.save(entity);
        return converter.toDTO(saved);
    }

    @GetMapping("/")
    public List<BlogSectionDTO> findAll() {
        return service.findAll().stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}