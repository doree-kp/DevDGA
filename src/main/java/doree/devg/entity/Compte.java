package doree.devg.entity;

import doree.devg.repository.CompteRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;

import java.time.LocalDate;

import java.util.List;

@Builder
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

    private String  typeCompte;
    private LocalDate dateCreation;
    private double solde;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @OneToMany(mappedBy = "compte")
    private List<Transaction> transactions;


    public void genererIban() {

            this.numeroCompte = Iban.random(CountryCode.FR).toString();

    }


}
