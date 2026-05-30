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
        
        // Clean up empty singular tables created by old schema.sql
        try {
            jdbcTemplate.execute("DROP TABLE IF EXISTS department, doctor, hospital");
            log.info("Successfully cleaned up empty singular tables.");
        } catch (Exception e) {
            log.debug("Failed to drop singular tables: {}", e.getMessage());
        }

        // Migrate user table
        String[] userCols = {"username", "email", "wechat_openid", "alipay_user_id"};
        for (String col : userCols) {
            try {
                jdbcTemplate.execute("ALTER TABLE users ADD COLUMN " + col + " VARCHAR(100) UNIQUE");
                log.info("Successfully added '{}' column to users table.", col);
            } catch (Exception e) {
                log.debug("Column '{}' might already exist or could not be added", col);
            }
        }

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
        
        // Add indices for search optimization
        try {
            jdbcTemplate.execute("CREATE INDEX idx_hospitals_name ON hospitals(name)");
            jdbcTemplate.execute("CREATE INDEX idx_hospitals_city ON hospitals(city)");
            log.info("Successfully added indexes to hospitals table.");
        } catch (Exception e) {
            log.debug("Indexes on hospitals might already exist");
        }

        try {
            jdbcTemplate.execute("CREATE INDEX idx_departments_name ON departments(name)");
            log.info("Successfully added index to departments table.");
        } catch (Exception e) {
            log.debug("Index on departments might already exist");
        }

        try {
            jdbcTemplate.execute("CREATE INDEX idx_doctors_name ON doctors(name)");
            jdbcTemplate.execute("CREATE INDEX idx_doctors_hospital_id ON doctors(hospital_id)");
            jdbcTemplate.execute("CREATE INDEX idx_doctors_department_id ON doctors(department_id)");
            log.info("Successfully added indexes to doctors table.");
        } catch (Exception e) {
            log.debug("Indexes on doctors might already exist");
        }

        log.info("Database migration completed.");
        
        try {
            jdbcTemplate.execute("ALTER TABLE hospitals ADD COLUMN level_name VARCHAR(50)");
            log.info("Successfully added 'level_name' column to hospitals table.");
        } catch (Exception e) {
            log.debug("Column 'level_name' might already exist in hospitals");
        }

        // Add new companion fields
        try {
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN id_card VARCHAR(100)");
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN driving_license VARCHAR(100)");
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN has_wheelchair TINYINT DEFAULT 0");
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN is_veteran TINYINT DEFAULT 0");
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN has_nursing_experience TINYINT DEFAULT 0");
            jdbcTemplate.execute("ALTER TABLE companions ADD COLUMN self_description TEXT");
            log.info("Successfully added new fields to companions table.");
        } catch (Exception e) {
            log.debug("New companion fields might already exist");
        }

        // Add order revenue fields
        try {
            jdbcTemplate.execute("ALTER TABLE orders ADD COLUMN platform_fee DECIMAL(10,2)");
            jdbcTemplate.execute("ALTER TABLE orders ADD COLUMN companion_income DECIMAL(10,2)");
            log.info("Successfully added revenue fields to orders table.");
        } catch (Exception e) {
            log.debug("Order revenue fields might already exist");
        }

        // Create companion wallets table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS companion_wallets (" +
                "companion_id BIGINT PRIMARY KEY, " +
                "balance DECIMAL(10,2) DEFAULT 0.00, " +
                "frozen_balance DECIMAL(10,2) DEFAULT 0.00, " +
                "total_revenue DECIMAL(10,2) DEFAULT 0.00, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")");

        // Create wallet transactions table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS wallet_transactions (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "companion_id BIGINT NOT NULL, " +
                "order_id BIGINT, " +
                "amount DECIMAL(10,2) NOT NULL, " +
                "type INT NOT NULL COMMENT '1=订单分润 2=提现支出 3=违规扣款', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");

        // Drop singular legacy tables if they exist
        String[] legacyTables = {"department", "doctor", "hospital"};
    }
}
