package doree.devg.repository;

import doree.devg.entity.Compte;
import doree.devg.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Compte findCompteByNumeroCompte(String numeroCompte);

//    <T> List<Operation> findByCompteIdAndMadeAtBetween(Long idCompte, LocalDate dateDebut, LocalDate dateFin, List<T> list);


}
