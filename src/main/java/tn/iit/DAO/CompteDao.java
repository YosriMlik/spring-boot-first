package tn.iit.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tn.iit.entite.Compte;

@Repository
public class CompteDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Compte compte) {
		entityManager.persist(compte);
		
	}
	
	public void update(Compte compte) {
		entityManager.merge(compte);
	}
	 public void delete(Compte compte) {
		 entityManager.remove(compte);
	 }
	 public Compte findById(Long rib) {
		 return entityManager.find(Compte.class, rib);
	 }
	 
	 public List<Compte> findAll() {

			// Le langage utilisé = EJB-QL != SQL, C'est comme SQL, mais orienté Objet

			return entityManager.createQuery("select c from Compte c", Compte.class).getResultList();

		}



		public List<Compte> findAllBySolde(float solde) {

			// Le langage utilisé = EJB-QL != SQL, C'est comme SQL, mais orienté Objet

			return entityManager.createQuery("select c from Compte c where c.solde > :solde", Compte.class)

					.setParameter("solde", solde).getResultList();

		}



		public List<Compte> findAll2() {

			// Le langage utilisé ici est SQL natif

			return entityManager.createNativeQuery("select * from t_compte c", Compte.class).getResultList();

		}

}
