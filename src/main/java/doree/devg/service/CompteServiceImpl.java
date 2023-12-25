package doree.devg.service;

import doree.devg.entity.Compte;
import doree.devg.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    @Override
    public void updateSolde(Compte compte, double nouveauSolde) {

    }
}
