package doree.devg.controller;

import doree.devg.repository.CompteRepository;
import doree.devg.service.CompteServiceImpl;
import doree.devg.service.TransactionService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transactions")

public class TransactionController {

    @Autowired
    private CompteServiceImpl compteService;

    @Autowired
    private TransactionService transactionService;
    @GetMapping("/depot")
    public ResponseEntity<String> makeDeposit(@RequestParam String numeroCompte, @RequestParam float montant){
        compteService.makeDeposit(numeroCompte, montant);
        return ResponseEntity.ok("Dépôt effectué avec succès.");
    }
    @GetMapping("/retrait")
    public ResponseEntity<String> makeWithdrawal(@RequestParam String numeroCompte, @RequestParam float montant){
        compteService.makeWithdrawal(numeroCompte, montant);
        return ResponseEntity.ok("Retrait effectué avec succès.");
    }

    @GetMapping("/transfert")
    public ResponseEntity<String > makeTransfert(
            @RequestParam String sourceNumeroCompte,
            @RequestParam String destinationNumeroCompte,
            @RequestParam float montant
    ){
        transactionService.makeTransfert(sourceNumeroCompte, destinationNumeroCompte, montant);
        return ResponseEntity.ok("Virement effectué avec succès.");
    }
}
