package doree.devg.service;

import doree.devg.entity.Client;

import java.util.List;

public interface IClientService {
    List<Client> getAllClients();
    Client getClientById(Long id);
    Client saveClient(Client client);
    void deleteClient(Long id);
    Client getClientByLoginInfo(String username, String password);
}
