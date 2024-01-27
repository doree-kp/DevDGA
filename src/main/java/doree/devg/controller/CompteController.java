package doree.devg.controller;

import doree.devg.extra.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.entity.Transaction;
import doree.devg.service.CompteServiceImpl;
import doree.devg.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import doree.devg.extra.RelevePDFGenerator;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    @Autowired
    private CompteServiceImpl compteService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping
    public List<Compte> getAllComptes(){
        return compteService.getAllComptes();
    }

    @GetMapping("/{id}")
    public Compte getCompteById(@PathVariable Long id){
        return compteService.getCompteById(id);
    }

    @PostMapping
    public Compte saveCompte(@RequestBody Compte compte){
        return compteService.saveCompte(compte);
    }

    @DeleteMapping("/{id}")
    public void deleteCompte(@PathVariable Long id){
        compteService.deleteCompte(id);
    }

    @PostMapping("/{id}/versement")
    public ResponseEntity<Transaction> faireVersement(@PathVariable Long id, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteById(id);
        if (compte != null){
            transaction.setCompte(compte);
            Transaction nouvelleTransaction = transactionService.saveTransaction(transaction);
            return ResponseEntity.ok(nouvelleTransaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/retrait")
    public ResponseEntity<Transaction> faireRetrait(@PathVariable Long id, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteById(id);
        if (compte != null){
            transaction.setCompte(compte);
            transaction.setType("RETRAIT");
            try {
                Transaction nouvelleTransaction = transactionService.saveTransaction(transaction);
                return ResponseEntity.ok(nouvelleTransaction);
            } catch (SoldeInsuffisantException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/virement")
    public ResponseEntity<Transaction> faireVirement(@PathVariable Long id, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteById(id);
        Compte compteDest = compteService.getCompteById(transaction.getCompteDest().getIdCompte());
        if (compte != null && compteDest != null){
            transaction.setCompte(compte);
            transaction.setCompteDest(compteDest);
            transaction.setType("VIREMENT");

            try {
                Transaction nouvelleTransaction = transactionService.saveTransaction(transaction);
                return ResponseEntity.ok(nouvelleTransaction);
            } catch (SoldeInsuffisantException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>>getTransactionsByCompteAndDate(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin){
        Compte compte = compteService.getCompteById(id);
        if (compte != null){
            List<Transaction> transactions = transactionService.getTransactionsByCompteAndDate(id, dateDebut, dateFin);
            return ResponseEntity.ok(transactions);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}/releve")
    public ResponseEntity<byte[]> genererReleve(@PathVariable Long id,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin){
        Compte compte = compteService.getCompteById(id);
        if (compte != null){
            List<Transaction> transactions = transactionService.getTransactionsByCompteAndDate(id, dateDebut, dateFin);
            byte[] relevePDF = genererRelevePDF(compte, transactions);

            return ResponseEntity.ok()
                    .header("content-Disposition", "attachment; filename=releve.pdf")
                    .body(relevePDF);

        }else {
            String message = "Aucun compte n'est trouvé avec l'ID : " + id;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private byte[] genererRelevePDF(Compte compte, List<Transaction> transactions){
        StringBuilder contenuReleve = new StringBuilder();
        contenuReleve.append("Relevé de compte pour le compte : ").append(compte.getNumeroCompte()).append("\n\n");

        for (Transaction transaction : transactions){
            contenuReleve.append(transaction.getType()).append(" : ").append(transaction.getMontant()).append("\n");
        }
        return RelevePDFGenerator.genererRelevePDF(contenuReleve.toString());
    }

}
