package com.telerikacademy.web.photocontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class HibernateConfig {
    private final String dbUrl, dbUsername, dbPassword, dbClassName;

    public HibernateConfig(Environment environment) {
        dbUrl = environment.getProperty("database.url");
        dbUsername = environment.getProperty("database.username");
        dbPassword = environment.getProperty("database.password");
        dbClassName = environment.getProperty("spring.datasource.driver-class-name");
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        // TODO add package
        sessionFactory.setPackagesToScan();
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName(dbClassName);
        return dataSource;
    }
}
