package com.example.security.dao;
import com.example.security.entity.BlogSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogSectionRepository extends JpaRepository<BlogSection, Long> {
}
