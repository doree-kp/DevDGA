package doree.devg.service;

import doree.devg.entity.Compte;

import java.util.List;

public interface ICompteService {
    List<Compte> getAllComptes();
    Compte getCompteById(Long id);
    Compte saveCompte(Compte compte);
    void deleteCompte(Long id);
    Compte getCompteByNumeroCompte(String numeroCompte);

    void updateSolde(String  numeroCompte, double montant);

}
