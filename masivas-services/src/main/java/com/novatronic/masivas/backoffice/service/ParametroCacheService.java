package com.novatronic.masivas.backoffice.service;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HazelcastInstance hazelcastInstance;

//    @Autowired
//    private TaDetalleParametroRepository detalleParametroRepository;
//    @Autowired
//    private TpConceptoLiquidacionBcrpRepository tpConceptoLiquidacionBcrpRepository;
//    @Autowired
//    private TsCodigoRespuestaRepository tsCodigoRespuestaRepository;
//    @Autowired
//    private TpFacilidadBcrpRepository TpFacilidadBcrpRepository;
//    @Autowired
//    private TpEntidadFinancieraBcrpRepository entidadFinancieraBcrpRepository;
    private static final NovaLogger LOGGER = NovaLogger.getLogger(ParametroCacheService.class);

    private static final String CLASSPATH_FILE = "nova-log.properties";
    private static final String EXTERNAL_FILE_RELATIVE = "/MSVBAS/nova-log.properties";

    @PostConstruct
    public void initCache() throws IOException {

        Properties novaProps = new Properties();

        Resource classpathResource = new ClassPathResource(CLASSPATH_FILE);
        // 1. Intentar cargar desde classpath
        if (classpathResource.exists()) {
            LOGGER.info("Cargando archivo de configuración desde classpath: {}", CLASSPATH_FILE);
            novaProps = PropertiesLoaderUtils.loadProperties(classpathResource);
            //return properties;
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

//        loadFacilidadesBcrpInCache();
//        loadParametersInCache();
//        loadConceptInCache();
//        loadResposneInCache();
//        loadEntidadesBcrpInCache();
    }

//    private void loadFacilidadesBcrpInCache() {
//        List<TpFacilidadBcrp> facilidades = TpFacilidadBcrpRepository.findAll();
//        IMap<String, TpFacilidadBcrp> cache = hazelcastInstance.getMap(ConstantesServices.MAP_FACILIDADES);
//        for (TpFacilidadBcrp cr : facilidades) {
//            cache.put(cr.getCodFacilidad(), cr);
//        }
//
//    }
//
//    private void loadResposneInCache() {
//        List<TsCodigoRespuesta> respuestas = tsCodigoRespuestaRepository.findAll();
//        IMap<String, TsCodigoRespuesta> cache = hazelcastInstance.getMap(ConstantesServices.MAP_CODIGOSRESPUESTA);
//
//        for (TsCodigoRespuesta cr : respuestas) {
//            cache.put(cr.getCodCodigoRspsta(), cr);
//        }
//
//    }
//
//    public void loadParametersInCache() {
//        Map<String, List<TaDetalleParametroDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_PARAMETROS);
//        List<TaDetalleParametro> parametros = detalleParametroRepository.findAll();
//
//        Map<String, List<TaDetalleParametroDTO>> agrupados = parametros.stream()
//                .filter(p -> p.getIdGrupoParametro() != null)
//                .collect(Collectors.groupingBy(
//                        p -> p.getIdGrupoParametro().getIdGrupoParametro(),
//                        Collectors.mapping(
//                                p -> new TaDetalleParametroDTO(p.getValorParametro(), p.getNomParametro()),
//                                Collectors.toList()
//                        )
//                ));
//
//        cache.putAll(agrupados);
//    }
//
//    public void loadConceptInCache() {
//        Map<String, List<ConceptoDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_CONCEPTOS);
//
//        List<Object[]> rawList = tpConceptoLiquidacionBcrpRepository.findAllConceptosRaw();
//
//        List<ConceptoDTO> conceptos = rawList.stream()
//                .map(obj -> new ConceptoDTO(
//                ((String) obj[0]).trim(), // codConcepto
//                ((String) obj[1]).trim(), // desConcepto
//                ((String) obj[2]).trim(), // codTransaccion
//                obj[3] != null ? ((Number) obj[3]).intValue() : null, // codPanel (Integer)
//                ((String) obj[4]).trim(), // codMoneda
//                ((String) obj[5]).trim(), // descripcionMoneda
//                ((String) obj[6]).trim() // simbolo
//        ))
//                .collect(Collectors.toList());
//
//        Map<String, List<ConceptoDTO>> agrupados = conceptos.stream()
//                .collect(Collectors.groupingBy(ConceptoDTO::getCodTransaccion));
//
//        cache.clear();
//        cache.putAll(agrupados);
//        cache.put("ALL", conceptos);
//    }
//
//    public List<ConceptoDTO> getConceptByTransaction(String codTransaccion) {
//        Map<String, List<ConceptoDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_CONCEPTOS);
//        if (codTransaccion == null || codTransaccion.isBlank() || "ALL".equalsIgnoreCase(codTransaccion)) {
//            return cache.getOrDefault("ALL", Collections.emptyList());
//        }
//        return cache.getOrDefault(codTransaccion, Collections.emptyList());
//    }
//
//    public List<TaDetalleParametroDTO> getParameteres(String idGrupo) {
//        Map<String, List<TaDetalleParametroDTO>> cache = hazelcastInstance.getMap(ConstantesServices.MAP_PARAMETROS);
//        return cache.getOrDefault(idGrupo, Collections.emptyList());
//    }
//
//    public List<TpFacilidadBcrp> getAllFacilidades() {
//        IMap<String, TpFacilidadBcrp> cache = hazelcastInstance.getMap(ConstantesServices.MAP_FACILIDADES);
//        return new ArrayList<>(cache.values());
//    }
//
//    public List<TpEntidadFinancieraBcrpDTO> getAllEntidadesConCuentas() {
//        IMap<String, TpEntidadFinancieraBcrpDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_ENTIDADES_BCRP);
//        return new ArrayList<>(cache.values());
//    }
//
//    public void loadEntidadesBcrpInCache() {
//        List<TpEntidadFinancieraBcrp> entidades = entidadFinancieraBcrpRepository.findAllWithCuentasCorrientes();
//
//        Map<String, TpEntidadFinancieraBcrpDTO> cache = hazelcastInstance.getMap(ConstantesServices.MAP_ENTIDADES_BCRP);
//
//        for (TpEntidadFinancieraBcrp entidad : entidades) {
//            String codigo = entidad.getCodEntidadFinanciera();
//            String nombreLargo = entidad.getNomCorto(); // asegúrate de tener este campo en tu entidad
//
//            List<TpCuentaCorrienteBcrpDTO> cuentas = entidad.getCuentasCorrientes().stream()
//                    .map(c -> new TpCuentaCorrienteBcrpDTO(
//                    c.getNumCuentaCorriente(),
//                    c.getNomCuentaCorriente(),
//                    c.getCodigoMoneda(),
//                    c.getIndLiquidadora(),
//                    c.getIndLiquidadoraFi(),
//                    c.getIndLiquidadoraOv()
//            )).collect(Collectors.toList());
//
//            TpEntidadFinancieraBcrpDTO dto = new TpEntidadFinancieraBcrpDTO(codigo, nombreLargo, cuentas);
//            cache.put(codigo, dto); // puedes usar código como key
//        }
//
//        LOGGER.info("Cargadas {} entidades financieras BCRP en caché", entidades.size());
//    }
}
