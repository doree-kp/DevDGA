package doree.devg.repository;

import doree.devg.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Compte findCompteByNumeroCompte(String numeroCompte);
}
