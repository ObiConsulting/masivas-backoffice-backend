package com.novatronic.masivas.backoffice.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Obi Consulting
 */
@Service
public class GenericService {

    @Autowired
    private ParametroCacheService parametroCacheService;

    @Autowired
    public GenericService() {
    }

//    public List<ConceptoDTO> findConceptosByCodTransaccion(String codTransaccion) {
//        return parametroCacheService.getConceptByTransaction(codTransaccion);
//    }
//
//    public List<TaDetalleParametroDTO> getAllDocuments() {
//        return parametroCacheService.getParameteres(ConstantesServices.ID_GRUPO_TIPO_DOCUMENTO);
//    }
//
//    public List<TaDetalleParametroDTO> getAllStatus() {
//        return parametroCacheService.getParameteres(ConstantesServices.ID_GRUPO_ESTADO);
//    }
//
//    public List<TaDetalleParametroDTO> getAllParticipants() {
//        return parametroCacheService.getParameteres(ConstantesServices.ID_GRUPO_TIPO_PARTICIPANTE);
//    }
//
//    public List<TpFacilidadBcrp> getAllFacilidades() {
//        return parametroCacheService.getAllFacilidades();
//    }
//
//    public String getConceptName(String conceptCode) {
//        List<ConceptoDTO> lista = parametroCacheService.getConceptByTransaction("ALL");
//        String nombreConcepto;
//        nombreConcepto = lista.stream()
//                .filter(concepto -> concepto.getCodConcepto().equals(conceptCode))
//                .findFirst()
//                .map(conceptoDTO -> conceptoDTO.getDesConcepto())
//                .orElse("");
//        return nombreConcepto;
//    }
//
//    public String getStatusName(String statusCode) {
//        List<TaDetalleParametroDTO> lista = parametroCacheService.getParameteres(ConstantesServices.ID_GRUPO_ESTADO);
//        String nombreEstado;
//        nombreEstado = lista.stream()
//                .filter(estado -> estado.getCodigo().equals(statusCode))
//                .findFirst()
//                .map(estadoDTO -> estadoDTO.getDescripcion())
//                .orElse("");
//        return nombreEstado;
//    }
//
//    public String getEntityName(String entityCode) {
//        List<TpEntidadFinancieraBcrpDTO> lista = parametroCacheService.getAllEntidadesConCuentas();
//        String nombreEntidad;
//        nombreEntidad = lista.stream()
//                .filter(entidad -> entidad.getCodigo().equals(entityCode))
//                .findFirst()
//                .map(entidadDTO -> entidadDTO.getDescripcion())
//                .orElse("");
//        return nombreEntidad;
//    }
}
