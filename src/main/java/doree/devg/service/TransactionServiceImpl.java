package doree.devg.service;

import doree.devg.extra.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.entity.Transaction;
import doree.devg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CompteServiceImpl compteService;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        Compte compte = transaction.getCompte();
        Compte compteDest = transaction.getCompteDest();
        double montant = transaction.getMontant();

        switch (transaction.getType()) {
            case "VERSEMENT":
                compteService.updateSolde(compte.getNumeroCompte(), +montant);
                break;
            case "RETRAIT":
                double nouveauSoldeRetrait = compte.getSolde() - montant;
                if (nouveauSoldeRetrait >= 0) {
                    compteService.updateSolde(compte.getNumeroCompte(), -montant);
                } else {
                    throw new SoldeInsuffisantException("Solde insuffisant pour le retrait.");
                }
                break;
            case "VIREMENT":
                double nouveauSoldeVirement = compte.getSolde() - montant;
                if (nouveauSoldeVirement >= 0) {
                    compteService.updateSolde(compte.getNumeroCompte(), -montant);

                    double nouveauSoldeDest = compteDest.getSolde() + montant;
                    compteService.updateSolde(compteDest.getNumeroCompte(), nouveauSoldeDest);
                } else {
                    throw new SoldeInsuffisantException("Solde insuffisant pour le virement.");
                }
                break;
        }

        transaction.setDate(LocalDate.now());
        return transactionRepository.save(transaction);
    }


    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> getTransactionsByCompteAndDate(String numeroCompte, Date dateDebut, Date dateFin) {
        return transactionRepository.findByCompte_NumeroCompteAndDateBetween(numeroCompte, dateDebut, dateFin);
    }
}
