package com.novatronic.masivas.backoffice.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.novatronic.masivas.backoffice.dto.EntidadDTO;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.entity.TpParametro;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import com.novatronic.masivas.backoffice.repository.GrupoParametroRepository;
import com.novatronic.masivas.backoffice.repository.ParametroRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Obi Consulting
 */
@Service
public class ParametroCacheService {

    private final HazelcastInstance hazelcastInstance;
    private final ParametroRepository parametroRepository;
    private final GrupoParametroRepository grupoParametroRepository;
    private final EntidadRepository entidadRepository;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ParametroCacheService.class);
    private static final String CLASSPATH_FILE = "nova-log.properties";
    private static final String EXTERNAL_FILE_RELATIVE = "/MSVBAS/nova-log.properties";

    public ParametroCacheService(HazelcastInstance hazelcastInstance, ParametroRepository parametroRepository, GrupoParametroRepository grupoParametroRepository, EntidadRepository entidadRepository) {
        this.hazelcastInstance = hazelcastInstance;
        this.parametroRepository = parametroRepository;
        this.grupoParametroRepository = grupoParametroRepository;
        this.entidadRepository = entidadRepository;
    }

    @PostConstruct
    public void initCache() throws IOException {

        Properties novaProps;

        Resource classpathResource = new ClassPathResource(CLASSPATH_FILE);
        // 1. Intentar cargar desde classpath
        if (classpathResource.exists()) {
            LOGGER.info("Cargando archivo de configuración desde classpath: {}", CLASSPATH_FILE);
            novaProps = PropertiesLoaderUtils.loadProperties(classpathResource);
        } else {
            // 2. Si no existe en classpath, intentar desde ruta externa
            String configPath = System.getenv("SIXCFG");
            if (configPath == null || configPath.isBlank()) {
                LOGGER.error("Variable de entorno SIXCFG no está definida.");
                throw new IOException("Variable de entorno SIXCFG no está definida.");
            }
            String externalFilePath = configPath + EXTERNAL_FILE_RELATIVE;
            Resource externalResource = new FileSystemResource(externalFilePath);
            if (!externalResource.exists()) {
                LOGGER.error("El archivo de configuración no existe: {}", externalFilePath);
                throw new IOException("El archivo de configuración no existe: " + externalFilePath);
            }
            novaProps = PropertiesLoaderUtils.loadProperties(externalResource);

        }
        NovaLogger.fromProperties(novaProps);

        loadParametersInCache();
        loadParametersGroupInCache();
        loadEntitiesInCache();
    }

    /**
     * Carga los parámetros del sistema en cache
     */
    public void loadParametersInCache() {
        Map<String, List<ParametroDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_PARAMETROS);
        List<TpParametro> parametros = parametroRepository.findAll();

        Map<String, List<ParametroDTO>> agrupados = parametros.stream()
                .filter(p -> p.getIdGrupoParametro() != null)
                .collect(Collectors.groupingBy(
                        p -> String.valueOf(p.getIdGrupoParametro()),
                        Collectors.mapping(
                                p -> {
                                    if (p.getIdGrupoParametro().equals(ConstantesServices.ID_GRUPO_EXTENSION_BASE_L) || p.getIdGrupoParametro().equals(ConstantesServices.ID_GRUPO_EXTENSION_CONTROL_L)) {
                                        return new ParametroDTO(String.valueOf(p.getIdParametro()), p.getValor());
                                    } else {
                                        return new ParametroDTO(p.getCodigo(), p.getValor());
                                    }
                                },
                                Collectors.toList()
                        )
                ));

        cache.putAll(agrupados);
    }

    /**
     * Carga los grupos parámetro del sistema en cache
     */
    public void loadParametersGroupInCache() {
        Map<String, ParametroDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_GRUPO_PARAMETROS);
        grupoParametroRepository.findAll().stream()
                .map(group -> new ParametroDTO(
                String.valueOf(group.getIdGrupoParametro()),
                group.getDescripcion()))
                .forEach(dto -> cache.put(dto.getCodigo(), dto));
    }

    /**
     * Carga las entidades financieras del sistema en cache
     */
    public void loadEntitiesInCache() {
        Map<String, EntidadDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_ENTIDADES);
        entidadRepository.findAll().stream()
                .map(entity -> new EntidadDTO(
                String.valueOf(entity.getCodigo()),
                entity.getNombre(),
                entity.getPropietario()))
                .forEach(dto -> cache.put(dto.getCodigo(), dto));
    }

    /**
     * Listado de los parámetros según grupo de parámetro de la caché.
     *
     * @param idGrupo
     * @return
     */
    public List<ParametroDTO> getParametersByGroup(String idGrupo) {
        Map<String, List<ParametroDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_PARAMETROS);
        List<ParametroDTO> parameterList = cache.getOrDefault(idGrupo, Collections.emptyList());

        if (!parameterList.isEmpty()) {
            parameterList.sort(Comparator.comparing(ParametroDTO::getCodigo));
        }

        return parameterList;
    }

    /**
     * Listado de los grupos parámetro de la caché.
     *
     * @return
     */
    public List<ParametroDTO> getAllParametersGroup() {
        loadParametersGroupInCache();
        Map<String, ParametroDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_GRUPO_PARAMETROS);
        return new ArrayList<>(cache.values());
    }

    /**
     * Obtener un grupo parámetro según código de la caché.
     *
     * @param codGroup
     * @return
     */
    public ParametroDTO getParameterGroup(String codGroup) {
        IMap<String, ParametroDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_GRUPO_PARAMETROS);
        return cache.get(codGroup);
    }

    /**
     * Listado de las entidades de la caché.
     *
     * @return
     */
    public List<EntidadDTO> getAllEntities() {
        Map<String, EntidadDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_ENTIDADES);
        return new ArrayList<>(cache.values());
    }

    /**
     * Obtener una entidad según código de la caché.
     *
     * @param codEntity
     * @return
     */
    public EntidadDTO getEntity(String codEntity) {
        IMap<String, EntidadDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_ENTIDADES);
        return cache.get(codEntity);
    }
}
