package doree.devg.service;

import doree.devg.entity.Transaction;

import java.util.Date;
import java.util.List;

public interface ITransactionService {
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
    Transaction saveTransaction(Transaction transaction);
    void deleteTransaction(Long id);
    List<Transaction> getTransactionsByCompteAndDate(Long idCompte, Date dateDebut, Date dateFin);
}
