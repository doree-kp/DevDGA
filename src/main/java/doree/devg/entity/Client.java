package doree.devg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String adresse;
    private String numeroTelephone;
    private String courriel;
    private String nationalite;
    private String username;
    private String password;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Compte> comptes;

    public void setPassword(String password){
        BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
