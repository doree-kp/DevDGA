package doree.devg.controller;

import doree.devg.entity.Operation;
import doree.devg.entity.Transfert;
import doree.devg.service.CompteServiceImpl;
import doree.devg.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @Autowired
//    TransactionService transactionService;
    @GetMapping("/getAllOperations/{id}")
    public List<Operation> getAllOperations(@PathVariable Long id){
        return transactionService.getAllOperations(id);
    }
    @GetMapping("/getAllTransfers/{id}")
    public List<Transfert> getAllTransfers(@PathVariable Long id){
        return transactionService.getAllTransferts(id);
    }
}
