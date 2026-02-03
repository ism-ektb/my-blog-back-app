package ru.ism.myblogbackapp.repository.Impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;


import javax.sql.DataSource;

//@Configuration
@Testcontainers
public class TestContainerConfig {

    static String url;
    static String user;
    static String password;
    static String driver;


    static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:13-alpine");

    static {
        postgreSQLContainer.start();
    }

    @Bean
    public DataSource database() {
        url = postgreSQLContainer.getJdbcUrl();
        user = postgreSQLContainer.getUsername();
        password = postgreSQLContainer.getPassword();
        driver = postgreSQLContainer.getDriverClassName();

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        return ds;
    }

}
