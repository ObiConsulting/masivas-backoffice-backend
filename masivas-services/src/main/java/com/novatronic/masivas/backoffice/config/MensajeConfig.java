package com.novatronic.masivas.backoffice.config;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *
 * @author Obi Consulting
 */
@Configuration
public class MensajeConfig {

    @Value("${mensajes.ruta:}")
    private String rutaExterna;

    @Value("${validaciones.ruta:}")
    private String rutaMensajesValidacion;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();

        System.out.println("ruta mensaje: " + rutaMensajesValidacion);
        if (rutaMensajesValidacion != null && !rutaMensajesValidacion.isBlank()
                && new File(rutaMensajesValidacion).exists()) {
            source.setBasename("file:" + rutaMensajesValidacion.replace("\\", "/").replace(".properties", ""));
            System.out.println("Usando ruta externa para validaciones: " + rutaMensajesValidacion);
        } else {
            source.setBasename("classpath:mensajesBeanValidation_es_PE");
            System.out.println("Usando ruta classpath para validaciones");
        }

        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean(name = "usuarioMessageSource")
    public MessageSource usuarioMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        System.out.println("ruta externa: " + rutaExterna);
        if (rutaExterna != null && !rutaExterna.isBlank() && new File(rutaExterna).exists()) {
            // Ruta externa (archivo del sistema)
            messageSource.setBasename("file:" + rutaExterna.replace("\\", "/").replace(".properties", ""));

            System.out.println("messageSource: " + messageSource.getBasenameSet());
        } else {
            // Ruta por defecto (classpath)
            messageSource.setBasename("classpath:mensaje");
        }
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource()); // <-- tu bean ya configurado
        return factoryBean;
    }
}
