package com.novatronic.masivas.backoffice.config;

import com.hazelcast.config.*;
import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Obi Consulting
 */
@Configuration
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ApplicationConfig {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ApplicationConfig.class);

    private static final String CLASSPATH_FILE = "aas.properties";
    private static final String EXTERNAL_FILE_RELATIVE = "/MSVBAS/aas.properties";

    @Value("#{'${cache.config.ips}'.split(',')}")
    private List<String> ips;

    @Value("${ip.autoincrement:false}")
    private Boolean autoincrementHazelcast;

    @Value("${ip.port:5701}")
    private Integer portHazelcast;

    @Value("${ip.portCount:3}")
    private Integer portCountHazelcast;

    @Value("${instanceName}")
    private String instanceName;

    @Value("${clusterName}")
    private String clusterName;

    @Value("${masivas.captcha.connecttimeout}")
    private int connectionTimeout;

    @Value("${masivas.captcha.readtimeout}")
    private int readTimeout;

    @Value("${mensajes.ruta:}")
    private String rutaExterna;

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }

    @Bean
    public Config hazelcastConfig() throws IOException {
        Config config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        network.setPort(portHazelcast);
        network.setPortAutoIncrement(autoincrementHazelcast);
        if (autoincrementHazelcast) {
            network.setPortCount(portCountHazelcast);
        }
        join.getTcpIpConfig().setMembers(this.ips);
        join.getTcpIpConfig().setEnabled(true);
        config.setClusterName(clusterName);
        return config.setInstanceName(instanceName);

    }

    @Bean(name = "aasProperties")
    public Properties aasProperties() throws IOException {
        Properties properties;

        // 1. Intentar cargar desde classpath
        Resource classpathResource = new ClassPathResource(CLASSPATH_FILE);
        if (classpathResource.exists()) {
            LOGGER.info("Cargando archivo de configuración desde classpath: {}", CLASSPATH_FILE);
            properties = PropertiesLoaderUtils.loadProperties(classpathResource);
            return properties;
        }

        // 2. Si no existe en classpath, intentar desde ruta externa
        String configPath = System.getenv("SIXCFG");
        if (configPath == null || configPath.isBlank()) {
            LOGGER.error(LogUtil.generarMensajeLogError("Variable de entorno SIXCFG no está definida."));
            throw new IOException();
        }

        String externalFilePath = configPath + EXTERNAL_FILE_RELATIVE;
        Resource externalResource = new FileSystemResource(externalFilePath);
        if (!externalResource.exists()) {
            LOGGER.error(LogUtil.generarMensajeLogError("El archivo de configuración no existe: "+externalFilePath));
            throw new IOException();
        }

        LOGGER.info("Cargando archivo de configuración desde ruta externa: {}", externalFilePath);
        properties = PropertiesLoaderUtils.loadProperties(externalResource);
        return properties;
    }

    @Bean(name = "usuarioMessageSource")
    public MessageSource usuarioMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        if (rutaExterna != null && !rutaExterna.isBlank() && new File(rutaExterna).exists()) {
            // Ruta externa (archivo del sistema)
            messageSource.setBasename("file:" + rutaExterna.replace("\\", "/").replace(".properties", ""));
        } else {
            // Ruta por defecto (classpath)
            messageSource.setBasename("classpath:mensaje");
        }
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
