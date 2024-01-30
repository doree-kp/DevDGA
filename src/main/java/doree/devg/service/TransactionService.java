package doree.devg.service;

import doree.devg.entity.Operation;
import doree.devg.entity.Transfert;
import doree.devg.entity.TypeOperation;
import doree.devg.extra.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.repository.CompteRepository;
import doree.devg.repository.OperationRepository;
import doree.devg.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private TransfertRepository transfertRepository;

//   public boolean makeDeposit(String numeroCompte, float montant){
//       Compte compte = compteRepository.findCompteByNumeroCompte(numeroCompte);
//       if (compte != null){
//           compte.setSolde(compte.getSolde() + montant);
//           Operation operation = new Operation();
//           operation.setCompte(compte);
//           operation.setMakedAt(LocalDateTime.now());
//           operation.setTypeOperation(TypeOperation.DEPOT);
//           operation.setMontant(montant);
//           operationRepository.save(operation);
//           compteRepository.save(compte);
//           return true;
//       }
//       return false;
//   }

   /*public void makeWithdrawal(String numeroCompte, float montant){
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
   }*/

   public void makeTransfert(String sourceNumeroCompte, String destinationNumeroCompte, float montant){
       Compte sourceCompte = compteRepository.findCompteByNumeroCompte(sourceNumeroCompte);
       Compte destinationCompte = compteRepository.findCompteByNumeroCompte(destinationNumeroCompte);

       if (sourceCompte != null && destinationCompte != null && sourceCompte.getSolde() >= montant){
           sourceCompte.setSolde(sourceCompte.getSolde() - montant);
           destinationCompte.setSolde(destinationCompte.getSolde() + montant);
           Transfert transfert = new Transfert();
           transfert.setMakedAt(LocalDate.now());
           transfert.setDestination(destinationCompte);
           transfert.setSource(sourceCompte);
           transfert.setMontant(montant);
           transfertRepository.save(transfert);
           compteRepository.save(sourceCompte);
           compteRepository.save(destinationCompte);
       }

   }
}
