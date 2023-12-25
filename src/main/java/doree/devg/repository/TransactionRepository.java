package doree.devg.repository;

import doree.devg.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCompte_IdCompteAndDateBetween(Long idCompte, Date dateDebut, Date dateFin);
}
