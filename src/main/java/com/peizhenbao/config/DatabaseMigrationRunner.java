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

        // Add status and created_at columns
        String[] tables = {"hospitals", "departments", "doctors"};
        for (String table : tables) {
            try {
                jdbcTemplate.execute("ALTER TABLE " + table + " ADD COLUMN status TINYINT DEFAULT 1");
                log.info("Successfully added 'status' column to {} table.", table);
            } catch (Exception e) {
                log.debug("Column 'status' might already exist in {} or could not be added", table);
            }
            try {
                jdbcTemplate.execute("ALTER TABLE " + table + " ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP");
                log.info("Successfully added 'created_at' column to {} table.", table);
            } catch (Exception e) {
                log.debug("Column 'created_at' might already exist in {} or could not be added", table);
            }
        }
        
        try {
            jdbcTemplate.execute("ALTER TABLE hospitals ADD COLUMN level_name VARCHAR(50)");
            log.info("Successfully added 'level_name' column to hospitals table.");
        } catch (Exception e) {
            log.debug("Column 'level_name' might already exist or could not be added");
        }
    }
}
