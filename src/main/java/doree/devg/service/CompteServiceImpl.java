package doree.devg.service;

import doree.devg.entity.Compte;
import doree.devg.entity.Operation;
import doree.devg.entity.TypeOperation;
import doree.devg.repository.CompteRepository;
import doree.devg.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CompteServiceImpl implements ICompteService{

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private OperationRepository operationRepository;

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

    public void makeWithdrawal(String numeroCompte, float montant){
        Compte compte = compteRepository.findCompteByNumeroCompte((numeroCompte));
        if (compte != null && compte.getSolde() >= montant){
            compte.setSolde(compte.getSolde() - montant);
            Operation operation = new Operation();
            operation.setCompte(compte);
            operation.setMakedAt(LocalDateTime.now());
            operation.setTypeOperation(TypeOperation.RETRAIT);
            operation.setMontant(montant);
            operationRepository.save(operation);
            compteRepository.save(compte);
        }
    }

    public boolean makeDeposit(String numeroCompte, float montant){
        Compte compte = compteRepository.findCompteByNumeroCompte(numeroCompte);
        if (compte != null){
            compte.setSolde(compte.getSolde() + montant);
            Operation operation = new Operation();
            operation.setCompte(compte);
            operation.setMakedAt(LocalDateTime.now());
            operation.setTypeOperation(TypeOperation.DEPOT);
            operation.setMontant(montant);
            operationRepository.save(operation);
            compteRepository.save(compte);
            return true;
        }
        return false;
    }

//    public List<Operation> getTransactions(Long idCompte, LocalDate dateDebut, LocalDate dateFin) {
//        return compteRepository.findByCompteIdAndMadeAtBetween(idCompte, dateDebut, dateFin, Arrays.asList(TypeOperation.DEPOT, TypeOperation.RETRAIT));
//    }


}
