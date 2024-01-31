package doree.devg.controller;

import doree.devg.dto.CompteDto;
import doree.devg.entity.Operation;
import doree.devg.extra.SoldeInsuffisantException;
import doree.devg.entity.Compte;
import doree.devg.repository.ClientRepository;
import doree.devg.service.CompteServiceImpl;
import doree.devg.service.TransactionService;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    @Autowired
    private CompteServiceImpl compteService;

    @Autowired
    private TransactionService transactionService;

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

    @GetMapping("/{idCompte}/transactions")
    public List<Operation> getTransactions(@PathVariable Long idCompte,
                                           @RequestParam("dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDebut,
                                           @RequestParam("dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFin) {
        return compteService.getTransactions(idCompte, dateDebut, dateFin);
    }

}
