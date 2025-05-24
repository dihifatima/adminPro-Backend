package com.example.security.service.impl;

import com.example.security.dao.BlogSectionRepository;
import com.example.security.entity.BlogSection;
import com.example.security.service.facade.BlogSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogSectionServiceImpl implements BlogSectionService {

    @Autowired
    private BlogSectionRepository repository;

    @Override
    public BlogSection save(BlogSection section) {
        return repository.save(section);
    }

    @Override
    public List<BlogSection> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
