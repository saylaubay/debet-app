package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Client;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public ApiResponse getAll() {
        List<Client> all = clientRepository.findAll();
        return new ApiResponse("Clients", true, all);
    }

    public ApiResponse getMyAll(User user) {
        List<Client> clients = clientRepository.findByCompany_Id(user.getCompany().getId());
        return new ApiResponse("My client list", true, clients);
    }
}
