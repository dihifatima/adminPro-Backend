package com.example.security.service.facade;

import com.example.security.entity.BlogSection;
import java.util.List;

public interface BlogSectionService {

    BlogSection save(BlogSection section);
    List<BlogSection> findAll();
    void deleteById(Long id);
}

