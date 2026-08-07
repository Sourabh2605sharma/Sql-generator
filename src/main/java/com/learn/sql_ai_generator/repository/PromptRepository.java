package com.learn.sql_ai_generator.repository;

import com.learn.sql_ai_generator.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
}