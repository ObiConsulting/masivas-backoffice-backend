package com.novatronic.masivas.backoffice.security.service;

import com.novatronic.masivas.backoffice.util.LogUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import com.novatronic.components.aas.AAService;
import com.novatronic.components.aas.AAServiceFactory;
import com.novatronic.components.aas.exception.AASException;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.util.Constantes;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class SCAService {

    private AAServiceFactory aaFactory;
    private AAService aaService;
    private List<String> resources;
    private List<String> profile;
    private Map<String, Object> parameters;
    private final Properties configProperties;

    @Autowired
    public SCAService(@Qualifier("aasProperties") Properties configProperties) {
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
            log.error(LogUtil.generarMensajeLogError("9999","Error AASException ",null), ex);
        } catch (IllegalStateException e) {
            log.error(LogUtil.generarMensajeLogError("9999",Constantes.MENSAJE_ERROR_BD_USUARIO,null), e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            aaService.shutdown();
            log.info("Cierre de recursos correctamente");
        } catch (AASException ex) {
            log.error(LogUtil.generarMensajeLogError("9999","Error al detener los servicios, ",null), ex);
        } catch (Exception e) {
            log.error(LogUtil.generarMensajeLogError("9999",Constantes.MENSAJE_ERROR_BD_USUARIO,null), e);
        }
    }

    public SCAResponseDto login(String empresa, String aplicacion, String usuario, String password) {

        InetAddress ip;
        String token;
        SCAResponseDto objRespuesta = new SCAResponseDto();
        String codigoRespuesta;

        try {

            ip = InetAddress.getLocalHost();
            String clientIP = ip.getHostAddress();

            parameters = new HashMap<>();
            parameters.put(AAService.APPLICATION_PARAM_KEY, aplicacion);
            parameters.put(AAService.PASSWORD_PARAM_KEY, password);
            parameters.put(AAService.USER_PARAM_KEY, usuario);

            //Parametros adicionales formato
            parameters.put(AAService.IP_TERMINAL_PARAM_KEY, clientIP);
            parameters.put(AAService.COMPANY_PARAM_KEY, empresa);

            //Generando el Token
            token = aaService.generateToken(parameters);
            codigoRespuesta = aaService.getResponCode();

            List<String> perfiles = listarProfiles(token);
            if (perfiles != null) {
                String rolName = perfiles.get(0);
                objRespuesta.setRolname(rolName);
            }
            objRespuesta.setToken(token);
            objRespuesta.setUsername(usuario);
            objRespuesta.setResponseCode(codigoRespuesta);
            objRespuesta.setResponseDescription(ConstantesServices.MENSAJE_EXITO_INICIAR_SESION);

        } catch (AASException ex) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ex.getExternalCode());
            objRespuesta.setResponseDescription(ex.getLocalizedMessage());

        } catch (UnknownHostException ex) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ConstantesServices.RESPUESTA_ERROR_9999);
            objRespuesta.setResponseDescription("Ocurrio un error al iniciar sesión");

        }
        return objRespuesta;
    }

    public Map<String, Object> getUserAttributes(String token) {
        Map<String, Object> mapaAtributos = null;
        try {
            mapaAtributos = aaService.getUserAttributes(token);
        } catch (AASException ex) {
            log.error(LogUtil.generarMensajeLogError(null,"Error al obtener lista de recursos, ",null), ex);
        } catch (Exception e) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, e);
        }
        return mapaAtributos;
    }

    public List<String> listarRecursos(String token) {
        try {
            resources = aaService.listResources(token);
        } catch (AASException ex) {
            log.error(LogUtil.generarMensajeLogError(null,"Error al obtener lista de recursos, ",null), ex);
        } catch (Exception e) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, e);
        }
        return resources;
    }

    public List<String> listarProfiles(String token) {
        try {
            profile = aaService.listProfiles(token);
        } catch (AASException ex) {
            log.error(LogUtil.generarMensajeLogError(null,"Error al obtener lista de Profiles, ",null), ex);
        } catch (Exception e) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, e);
        }
        return profile;
    }

    public SCAResponseDto changePassword(String empresa, String password, String nuevoPassword, String usuario) {

        InetAddress ip;
        SCAResponseDto objRespuesta = new SCAResponseDto();

        try {

            ip = InetAddress.getLocalHost();
            String clientIP = ip.getHostAddress();

            //Parametros adicionales formato 
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            parameters.put(AAService.IP_TERMINAL_PARAM_KEY, clientIP);
            parameters.put(AAService.COMPANY_PARAM_KEY, empresa);
            parameters.put(AAService.PASSWORD_PARAM_KEY, password);
            parameters.put(AAService.USER_PARAM_KEY, usuario);
            parameters.put(AAService.NEW_PASSWORD_PARAM_KEY, nuevoPassword);

            aaService.changePassword(parameters);

            objRespuesta.setResponseCode(ConstantesServices.RESPUESTA_OK_API);
            objRespuesta.setResponseDescription(ConstantesServices.MENSAJE_EXITO_CAMBIAR_CONTRASENA);

        } catch (AASException ex) {
            log.error(LogUtil.generarMensajeLogError("9999","Error al generar token. ",null), ex);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ex.getExternalCode());
            objRespuesta.setResponseDescription(ex.getCause().fillInStackTrace().getMessage());

        } catch (UnknownHostException e) {
            log.error(Constantes.MENSAJE_ERROR_BD_USUARIO, e);
            objRespuesta.setToken("");
            objRespuesta.setResponseCode(ConstantesServices.RESPUESTA_ERROR_9999);
            objRespuesta.setResponseDescription("Ocurrio un error al cambiar contraseña");
        }
        return objRespuesta;
    }

    public void disableToken(String user, String token) {
        aaService.disableToken(user, token);
    }

}
