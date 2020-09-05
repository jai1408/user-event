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
        String DATABASE_URL = "postgres://vistowngfxmvck:7104edd07be15db6d565bcc827205d616c95471e36e6ad0c114a67f8d14ec251@ec2-34-237-89-96.compute-1.amazonaws.com:5432/d3cc024fgj4mnc";
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