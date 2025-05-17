/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.novatronic.masivas.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author Obi Consulting
 */
//@PropertySources({
//    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
//    @PropertySource(value = "file:${SIXCFG}/LBTRWEB/application.properties", ignoreResourceNotFound = false)
//})
@ComponentScan(basePackages = {"com.novatronic"})
@SpringBootApplication
public class MasivasBackend extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MasivasBackend.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MasivasBackend.class);
    }
}
