package doree.devg.repository;

import doree.devg.entity.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {
    List<Transfert> findTransfertsBySource_IdCompte(Long id);
}
