package doree.devg.repository;

import doree.devg.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>  {
    Client findByUsernameAndPassword(String username, String password);
}
