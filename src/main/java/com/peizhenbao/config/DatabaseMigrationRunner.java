package com.peizhenbao.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Checking and applying database schema migrations...");
        try {
            jdbcTemplate.execute("ALTER TABLE hospitals ADD COLUMN phone VARCHAR(50)");
            log.info("Successfully added 'phone' column to hospitals table.");
        } catch (Exception e) {
            log.debug("Column 'phone' might already exist or could not be added: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE hospitals ADD COLUMN image VARCHAR(255)");
            log.info("Successfully added 'image' column to hospitals table.");
        } catch (Exception e) {
            log.debug("Column 'image' might already exist or could not be added: {}", e.getMessage());
        }
    }
}
