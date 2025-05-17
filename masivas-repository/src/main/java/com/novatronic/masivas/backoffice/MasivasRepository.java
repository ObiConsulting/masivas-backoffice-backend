package com.novatronic.masivas.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Obi Consulting
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.novatronic"})
public class MasivasRepository {

    public static void main(String[] args) {
        SpringApplication.run(MasivasRepository.class, args);
    }
}
