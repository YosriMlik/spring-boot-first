package tn.iit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tn.iit.DAO.ClientRepository;
import tn.iit.entite.Client;


@Service
public class ClientService implements UserDetailsService {
	 @Autowired
	    private ClientRepository clientRepository;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Client client = (Client) clientRepository.findByUsername(username);

	        if (client != null) {
	            return User.withUsername(client.getUsername())
	                       .password(client.getPassword())
	                       .roles(client.getRole())
	                       .build();
	        }

	        throw new UsernameNotFoundException("Client not found with username: " + username);
	    }
	   /* public List<Client> searchUsers(String searchTerm) {
	        return repo.searchUsers(searchTerm);
	    }*/
	    
	    public List<Client> getAllAccounts(){
	    	return clientRepository.findAll();
	    }
 public Client createClient(Client client) {
	 return  clientRepository.save(client);
 }
 public Optional<Client> findById(Integer id){
	 return clientRepository.findById(id);
 }
 public void deleteById(Integer id) {
	    if (clientRepository.existsById(id)) {
	        clientRepository.deleteById(id);
	    } else {
	        throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
	    }
	} 
 public Client updateClient(Integer id, Client updatedClient) {
	    return clientRepository.findById(id)
	            .map(existingClient -> {
	            	existingClient.setFirstName(updatedClient.getFirstName());
	            	existingClient.setLastName(updatedClient.getLastName());
	            	existingClient.setEmail(updatedClient.getEmail());
	            	existingClient.setPassword(updatedClient.getPassword());
	            	existingClient.setRole(updatedClient.getRole());
	                return clientRepository.save(existingClient);
	            })
	            .orElseThrow(() -> new IllegalArgumentException("Article with ID " + id + " does not exist."));
	}
}
