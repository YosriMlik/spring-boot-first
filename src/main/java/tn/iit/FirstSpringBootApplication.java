package tn.iit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import tn.iit.beans.Hello;
import tn.iit.entite.Compte;

@SpringBootApplication
public class FirstSpringBootApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(FirstSpringBootApplication.class, args);
	}
@Transactional //commit
	@Override
	//vient de la methode implement commandelinerunner
	public void run(String... args) throws Exception {
		
	}

}
