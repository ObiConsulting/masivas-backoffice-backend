package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.service.GenericService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "generico", produces = "application/json")
@RestController
public class GenericController {

    @Autowired
    GenericService genericService;

//    @RequestMapping(value = "/concepto/conceptoxtransaccion", method = RequestMethod.GET)
//    public ResponseEntity<LBTRResponse> findConceptByTxn(@RequestParam String codigoTransaccion) throws Exception {
//
//        LBTRResponse res;
//        List<ConceptoDTO> list;
//        try {
//            list = genericService.findConceptosByCodTransaccion(codigoTransaccion);
//            res = new LBTRResponse("0000", "Consulta OK", list);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//
//        } catch (Exception e) {
//            list = Collections.emptyList();
//            res = new LBTRResponse("0099", "Ocurrio un Error", list);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }
//
//    @RequestMapping(value = "/parametro/tipodocumentos", method = RequestMethod.GET)
//    public ResponseEntity<LBTRResponse> getAllDocuments() throws Exception {
//        LBTRResponse res;
//        List<TaDetalleParametroDTO> list;
//        try {
//            list = genericService.getAllDocuments();
//            res = new LBTRResponse("0000", "Consulta OK", list);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//        } catch (Exception e) {
//            list = Collections.emptyList();
//            res = new LBTRResponse("0099", "Ocurrio un Error", list);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }
//
//    @RequestMapping(value = "/parametro/estados", method = RequestMethod.GET)
//    public ResponseEntity<LBTRResponse> getAllStatus() throws Exception {
//        LBTRResponse res;
//        List<TaDetalleParametroDTO> list;
//        try {
//            list = genericService.getAllStatus();
//            res = new LBTRResponse("0000", "Consulta OK", list);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//        } catch (Exception e) {
//            list = Collections.emptyList();
//            res = new LBTRResponse("0099", "Ocurrio un Error", list);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }
//
//    @RequestMapping(value = "/parametro/tipoparticipante", method = RequestMethod.GET)
//    public ResponseEntity<LBTRResponse> getAllParticipants() throws Exception {
//        LBTRResponse res;
//        List<TaDetalleParametroDTO> list;
//        try {
//            list = genericService.getAllParticipants();
//            res = new LBTRResponse("0000", "Consulta OK", list);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//        } catch (Exception e) {
//            list = Collections.emptyList();
//            res = new LBTRResponse("0099", "Ocurrio un Error", list);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }
//
//    @RequestMapping(value = "/facilidades", method = RequestMethod.GET)
//    public ResponseEntity<LBTRResponse> findConceptByTxn() throws Exception {
//
//        LBTRResponse res;
//        List<TpFacilidadBcrp> list;
//        try {
//            list = genericService.getAllFacilidades();
//            res = new LBTRResponse("0000", "Consulta OK", list);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//
//        } catch (Exception e) {
//            list = Collections.emptyList();
//            res = new LBTRResponse("0099", "Ocurrio un Error", list);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }

}
