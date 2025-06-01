package com.novatronic.masivas.backoffice;

import com.novatronic.masivas.backoffice.entity.TpEntidad;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author obi
 */
@SpringBootTest
public class MasivasRepositoryTest {

    @Autowired
    private EntidadRepository entidadRepository;

    @Test
    public void listaEntidad() {

        List<TpEntidad> listaEntidad;
        listaEntidad = entidadRepository.findAll();
        System.out.println("result: " + listaEntidad);

    }

    @Test
    public void crearEntidad() {

        TpEntidad tpEntidad = new TpEntidad();

        tpEntidad.setCodigo("159");
        tpEntidad.setNombre("BANCO nuevo 159");
        tpEntidad.setEstado("0");
        tpEntidad.setIdPerfil(1L);
        tpEntidad.setFecCreacion(new Date());
        tpEntidad.setUsuCreacion("rvargas");

        System.out.println("before insert: ");
        TpEntidad tpEntidadSaved = entidadRepository.save(tpEntidad);
        System.out.println("result insert: " + tpEntidadSaved);

    }

    @Test
    public void updateEntidad() {

        Optional<TpEntidad> opt = entidadRepository.getByCodigo("0002");
        if (opt.isPresent()) {
            TpEntidad tpEntidad = opt.get();
            tpEntidad.setNombre("BANCO DE CREDITO DEL PERU2");
            tpEntidad.setFecModificacion(new Date());
            tpEntidad.setUsuModificacion("rvargas");
            TpEntidad tpEntidadSaved = entidadRepository.save(tpEntidad);
            System.out.println("result update: " + tpEntidadSaved);

        }

    }

    @Test
    public void buscar() {

        Pageable pageable = PageRequest.of(1, 5, Sort.by("cod_Entidad").ascending());
        Page<TpEntidad> resultados = entidadRepository.buscarPorFiltros("", "", "", pageable);
        for (TpEntidad resultado : resultados) {

            System.out.println("resultado" + resultado + "\n");

        }
        System.out.println("total elementos: " + resultados.getTotalElements());
        System.out.println("total Paginas: " + resultados.getTotalPages());
        System.out.println("Numero Elemntos: " + resultados.getNumberOfElements());

    }

}
