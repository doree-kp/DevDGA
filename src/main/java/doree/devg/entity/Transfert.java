package doree.devg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destination")

    @JsonIgnore
    private Compte destination;

    @ManyToOne
    @JoinColumn(name="source")
    @JsonIgnore
    private Compte source;
    private float montant;
    private LocalDate makedAt;
    private String destNumber;

}
