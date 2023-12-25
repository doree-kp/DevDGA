package doree.devg.service;

import doree.devg.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.entity.Transaction;
import doree.devg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class TransactionServiceImpl implements ITransactionService{
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
        double nouveauSolde = compte.getSolde();

        if ("VERSEMENT".equals(transaction.getType())){
            nouveauSolde += transaction.getMontant();
        } else if ("RETRAIT".equals(transaction.getType())){
            if (nouveauSolde >= transaction.getMontant()){
                nouveauSolde -= transaction.getMontant();
            }else {
                throw new SoldeInsuffisantException("Solde insuffisant pour le retrait. ");
            }
        } else if ("VIREMENT".equals(transaction.getType())) {
            if (nouveauSolde >= transaction.getMontant()){
                nouveauSolde -= transaction.getMontant();
                Compte compteDestinataire = transaction.getCompteDest();
                double nouveauSoldeDest = compteDestinataire.getSolde() + transaction.getMontant();
                compteService.updateSolde(compteDestinataire, nouveauSoldeDest);
            }else {
                throw new SoldeInsuffisantException("Solde insuffisant pour lr virement. ");
            }

        }

        compteService.updateSolde(compte, nouveauSolde);

        transaction.setDate(new Date());
        return transactionRepository.save(transaction);
    }



    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> getTransactionsByCompteAndDate(Long idCompte, Date dateDebut, Date dateFin) {
        return transactionRepository.findByCompte_IdCompteAndDateBetween(idCompte, dateDebut, dateFin);
    }
}
