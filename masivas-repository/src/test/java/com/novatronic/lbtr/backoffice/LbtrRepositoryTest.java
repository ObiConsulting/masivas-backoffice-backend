package com.novatronic.lbtr.backoffice;

import com.novatronic.masivas.backoffice.repository.RepoOvernightRepository;
import com.novatronic.masivas.backoffice.repository.TmMonedaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author obi
 */
@SpringBootTest
public class LbtrRepositoryTest {

    @Autowired
    private TmMonedaRepository tmMonedaRepository;

    @Autowired
    private RepoOvernightRepository repoOvernnightRepository;

    @Test
    public void contextLoads() {

//        List<TmMoneda> resultMoenda;
//        resultMoenda = tmMonedaRepository.findAll();
//        System.out.println("result: " + resultMoenda);
//        List<ConceptoView> resultConcepto;
//        resultConcepto = tpConceptoLiquidacionBcrpRepository.findConcepByCodTransaction("CV");
//        System.out.println("result: " + resultConcepto);
    }

}
