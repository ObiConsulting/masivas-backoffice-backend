package com.novatronic.masivas.backoffice.security.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import com.novatronic.components.aas.AAService;
import com.novatronic.components.aas.AAServiceFactory;
import com.novatronic.components.aas.exception.AASException;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

/**
 *
 * @author Obi Consulting
 */
@Slf4j
@Service
public class SCAAutentication {

    private AAServiceFactory aaFactory;
    private AAService aaService;
    private List<String> resources;
    private List<String> profile;
    private Map<String, Object> parameters;
    private Properties configProperties;

    @Autowired
    public SCAAutentication(@Qualifier("aasProperties") Properties configProperties) {
        this.configProperties = configProperties;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Inicializando ManejadorAutenticacion...");

            if (configProperties == null) {
                log.error("Las propiedades AAS no fueron inyectadas correctamente.");
                throw new IllegalStateException("Las propiedades AAS son nulas.");
            }
            log.info("Propiedades AAS cargadas correctamente.");

            aaFactory = AAServiceFactory.fromProperties(configProperties);
            log.info("Factory creada correctamente.");

            aaService = aaFactory.getAAServiceInstance();
            log.info("Instancia de AAService obtenida.");

            aaService.startup();
            log.info("Inicio Recursos Correctamente");

        } catch (AASException ex) {
            log.error("Error AASException ", ex);
        } catch (Exception e) {
            log.error("Error Generico ", e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            aaService.shutdown();
            log.info("Cierre de recursos correctamente");
        } catch (AASException ex) {
            log.error("Error al detener los servicios, ", ex);
        } catch (Exception e) {
            log.error("Error General, ", e);
        }
    }

    public SCAResponseDto login(String empresa, String aplicacion, String usuario, String password) {

        InetAddress ip;

        String token;
        SCAResponseDto objRespuesta = new SCAResponseDto();
        String codigoRespuesta;
        String rolname = "";
        try {
            ip = InetAddress.getLocalHost();
            String clientIP = ip.getHostAddress();
            log.debug("Cliente IP: " + clientIP);
            parameters = new HashMap<String, Object>();
            parameters.put(AAService.APPLICATION_PARAM_KEY, aplicacion);
            parameters.put(AAService.PASSWORD_PARAM_KEY, password);
            parameters.put(AAService.USER_PARAM_KEY, usuario);

            //Parametros adicionales formato 2
            parameters.put(AAService.IP_TERMINAL_PARAM_KEY, clientIP);
            parameters.put(AAService.COMPANY_PARAM_KEY, empresa);

            //Generando el Token
            log.info("Consultando usuario con parametros=[{}]" + parameters);
            token = aaService.generateToken(parameters);
            log.debug("Username: " + usuario + " tokenSCA: " + token);
            codigoRespuesta = aaService.getResponCode();
            log.debug("Codigo de Login " + codigoRespuesta);

            List<String> perfiles = listarProfiles(token);
            if (perfiles != null) {
                rolname = perfiles.get(0);
                objRespuesta.setRolname(rolname);
            }
            objRespuesta.setToken(token);
            objRespuesta.setUsername(usuario);
            objRespuesta.setResponseCode(codigoRespuesta);
            objRespuesta.setResponseDescription("Autenticacion OK");

        } catch (AASException ex) {
            log.error("Error Generico ", ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ex.getExternalCode());
            objRespuesta.setResponseDescription(ex.getLocalizedMessage());

        } catch (Exception ex) {
            log.error("Error Generico ", ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode("9999");
            objRespuesta.setResponseDescription("ERROR EN AUTENTICACION");

        }
        log.info("objRespuesta ->{}", objRespuesta);
        return objRespuesta;
    }

    public Map<String, Object> getUserAttributes(String token) {
        Map<String, Object> mapaAtributos = null;
        try {
            log.debug("tokenSCA: " + token);
            mapaAtributos = aaService.getUserAttributes(token);
        } catch (AASException ex) {
            log.error("Error al obtener lista de recursos, ", ex);
        } catch (Exception e) {
            log.error("Error General, ", e);
        }
        return mapaAtributos;
    }

    public List<String> listarRecursos(String token) {
        try {
            log.debug("tokenSCA: " + token);
            resources = aaService.listResources(token);
        } catch (AASException ex) {
            log.error("Error al obtener lista de recursos, ", ex);
        } catch (Exception e) {
            log.error("Error General, ", e);
        }
        return resources;
    }

    public List<String> listarProfiles(String token) {
        try {
            log.debug("tokenSCA: " + token);
            profile = aaService.listProfiles(token);
        } catch (AASException ex) {
            log.error("Error al obtener lista de Profiles, ", ex);
        } catch (Exception e) {
            log.error("Error General, ", e);
        }
        return profile;
    }

    public SCAResponseDto changePassword(String empresa, String password, String nuevoPassword, String usuario) {
        InetAddress ip;
        SCAResponseDto objRespuesta = new SCAResponseDto();
        try {
            ip = InetAddress.getLocalHost();
            String clientIP = ip.getHostAddress();
            log.debug("Cliente IP: " + clientIP);
            //Parametros adicionales formato 2
            parameters.put(AAService.IP_TERMINAL_PARAM_KEY, clientIP);
            parameters.put(AAService.COMPANY_PARAM_KEY, empresa);
            log.debug("Usuario para Change Password " + usuario);

            parameters.put(AAService.PASSWORD_PARAM_KEY, password);
            parameters.put(AAService.USER_PARAM_KEY, usuario);
            parameters.put(AAService.NEW_PASSWORD_PARAM_KEY, nuevoPassword);

            aaService.changePassword(parameters);

            objRespuesta.setResponseCode(ConstantesServices.RESPUESTA_OK_API);
            objRespuesta.setResponseDescription(ConstantesServices.MENSAJE_EXITO_CAMBIO_CLAVE);

        } catch (AASException ex) {
            log.error("Error al generar token, ", ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ex.getExternalCode());
            objRespuesta.setResponseDescription(ex.getCause().fillInStackTrace().getMessage());

        } catch (Exception e) {
            log.error("Error General, ", e);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode("9999");
            objRespuesta.setResponseDescription("Ocurrio un Error en Cambio de Clave");
        }
        return objRespuesta;
    }

    public void disableToken(String user, String token) {
        aaService.disableToken(user, token);
    }

}
