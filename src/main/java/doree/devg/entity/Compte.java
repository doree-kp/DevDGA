package doree.devg.entity;

import doree.devg.repository.CompteRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comptes")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompte;

    @Column(unique = true)
    private String numeroCompte;

    private String typeCompte;
    private Date dateCreation;
    private double solde;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @OneToMany(mappedBy = "compte")
    private List<Transaction> transactions;


    public void genererIban() {
        try {
            // Utilisez le code du pays TG (Togo) pour le pays, et une banque fictive
            Iban iban = new Iban.Builder()
                    .countryCode(CountryCode.TG)
                    .bankCode("1234")
                    .branchCode("5678")
                    .accountNumber(UUID.randomUUID().toString()) // Générez une partie aléatoire
                    .build();
            this.numeroCompte = iban.toString();
        } catch (IbanFormatException e) {
            e.printStackTrace();
            // Gérer l'exception si nécessaire
        }
    }


}
