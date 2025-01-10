package tn.iit.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.iit.entite.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	  /*@Query("SELECT u FROM AppUser u WHERE " +
	            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
	            "(LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
	            "(LOWER(u.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
	            "(LOWER(u.specialty) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
	     List<AppUser> searchUsers(@Param("searchTerm") String searchTerm);*/
		
		 Client findByUsername(String username) ;
		 Client findByEmail(String email) ;

}
