package tn.iit.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.iit.entite.Compte;

public interface CompteRepository extends JpaRepository<Compte, Integer> {
	   List<Compte> findByClient_Id(int clientId); 
	   Compte findByRib(Long rib);

}
