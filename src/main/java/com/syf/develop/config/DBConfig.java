package com.syf.develop.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DBConfig {

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        String DATABASE_URL = "postgres://iyhpxrqfwkdgig:b5f7279f5817680dd95a8a057e32f9d8532181bc77dbb70cb0e5a292c7ceb2dd@ec2-34-237-89-96.compute-1.amazonaws.com:5432/d8kkenotq6kose";
        URI dbUri = new URI(DATABASE_URL);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}