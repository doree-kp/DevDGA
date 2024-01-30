package doree.devg.controller;

import doree.devg.dto.CompteDto;
import doree.devg.entity.Client;
import doree.devg.entity.TypeCompte;
import doree.devg.extra.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.entity.Transaction;
import doree.devg.repository.ClientRepository;
import doree.devg.service.CompteServiceImpl;
import doree.devg.service.TransactionServiceImpl;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import doree.devg.extra.RelevePDFGenerator;
import java.time.LocalDate;


import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    @Autowired
    private CompteServiceImpl compteService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Compte> getAllComptes(){
        return compteService.getAllComptes();
    }

    @GetMapping("/{numeroCompte}")
    public Compte getCompteByNumeroCompte(@PathVariable String  numeroCompte){
        return compteService.getCompteByNumeroCompte(numeroCompte);
    }

    @PostMapping
    public Compte saveCompte(@RequestBody CompteDto compteDto){
        var client = clientRepository.findById(compteDto.getIdClient()).get();
        Compte compte = Compte.builder().typeCompte(compteDto.getType()).solde(0.0).dateCreation(LocalDate.now()).numeroCompte(Iban.random(CountryCode.FR).toString()).client(client).build();
        return compteService.saveCompte(compte);
    }

    @DeleteMapping("/{id}")
    public void deleteCompte(@PathVariable Long id){
        compteService.deleteCompte(id);
    }

    @PostMapping("/{numeroCompte}/versement")
    public ResponseEntity<Transaction> faireVersement(@PathVariable String  numeroCompte, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteByNumeroCompte(numeroCompte);
        if (compte != null){
            transaction.setCompte(compte);
            Transaction nouvelleTransaction = transactionService.saveTransaction(transaction);
            return ResponseEntity.ok(nouvelleTransaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{numeroCompte}/retrait")
    public ResponseEntity<Transaction> faireRetrait(@PathVariable String numeroCompte, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteByNumeroCompte(numeroCompte);
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

    @PostMapping("/{numeroCompte}/virement")
    public ResponseEntity<Transaction> faireVirement(@PathVariable String numeroCompte, @RequestBody Transaction transaction){
        Compte compte = compteService.getCompteByNumeroCompte(numeroCompte);
        Compte compteDest = compteService.getCompteByNumeroCompte(transaction.getCompteDest().getNumeroCompte());
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

    @GetMapping("/{numeroCompte}/transactions")
    public ResponseEntity<List<Transaction>>getTransactionsByCompteAndDate(
            @PathVariable String  numeroCompte,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin){
        Compte compte = compteService.getCompteByNumeroCompte(numeroCompte);
        if (compte != null){
            List<Transaction> transactions = transactionService.getTransactionsByCompteAndDate(numeroCompte, dateDebut, dateFin);
            return ResponseEntity.ok(transactions);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{numeroCompte}/releve")
    public ResponseEntity<byte[]> genererReleve(@PathVariable String numeroCompte,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin){
        Compte compte = compteService.getCompteByNumeroCompte(numeroCompte);
        if (compte != null){
            List<Transaction> transactions = transactionService.getTransactionsByCompteAndDate(numeroCompte, dateDebut, dateFin);
            byte[] relevePDF = genererRelevePDF(compte, transactions);

            return ResponseEntity.ok()
                    .header("content-Disposition", "attachment; filename=releve.pdf")
                    .body(relevePDF);

        }else {
            String message = "Aucun compte n'est trouvé avec le numero du compte : " + numeroCompte;
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
