package doree.devg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    private double montant;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "numeroCompte")
    private Compte compte;

    private String type;

    @ManyToOne
    @JoinColumn(name = "numeroCompteDest")
    private Compte compteDest;
}
