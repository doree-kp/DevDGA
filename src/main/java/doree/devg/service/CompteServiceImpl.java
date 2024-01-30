package doree.devg.service;

import doree.devg.entity.Compte;
import doree.devg.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompteServiceImpl implements ICompteService{

    @Autowired
    private CompteRepository compteRepository;
    @Override
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    @Override
    public Compte getCompteById(Long id) {
        return compteRepository.findById(id).orElse(null);
    }

    @Override
    public Compte getCompteByNumeroCompte(String  numeroCompte) {
        return compteRepository.findCompteByNumeroCompte(numeroCompte);
    }

    @Override
    public Compte saveCompte(Compte compte) {
        compte.genererIban();
        return compteRepository.save(compte);
    }

    @Override
    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    @Override
    public void updateSolde(String numeroCompte, double montant) {
        Compte compte = compteRepository.findCompteByNumeroCompte(numeroCompte);
        if (compte!= null){
            double nouveauSolde = compte.getSolde() + montant;
            compte.setSolde(nouveauSolde);
            compteRepository.save(compte);
        }else {
            throw new IllegalArgumentException("Compte non trouvé avec le numéro de compte : " + numeroCompte);
        }
    }

}
