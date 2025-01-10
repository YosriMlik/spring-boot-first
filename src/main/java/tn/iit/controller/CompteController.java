package tn.iit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import tn.iit.DAO.ClientRepository;
import tn.iit.DAO.CompteRepository;
import tn.iit.dto.CompteDto;
import tn.iit.entite.Client;
import tn.iit.entite.Compte;
import tn.iit.exception.CompteNotFoundException;
import tn.iit.service.CompteService;

@AllArgsConstructor
@RestController
@RequestMapping("/comptes")
public class CompteController {
	@Autowired

	private CompteService compteService;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private ClientRepository clientRepository;

	/*
	 * @GetMapping public List<Compte> findAll() {
	 * 
	 * return compteRepository.findAll(); }
	 */
	@GetMapping
	public List<Compte> findAllJson() {
		return compteRepository.findAll();
	}

	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<Compte>> getAppointments(@PathVariable int clientId) {
		List<Compte> compte = compteRepository.findByClient_Id(clientId);
		return ResponseEntity.ok(compte);
	}

	@PostMapping
	public ResponseEntity<String> save(@RequestBody CompteDto compteDto) {
		Compte compte = new Compte();
		Client client = clientRepository.findById(compteDto.getClientId()).orElseThrow();
		compte.setClient(client);
		compte.setCreationDate(compteDto.getCreationDate());
		compte.setSolde(compteDto.getSolde());
		compteRepository.save(compte);
		return ResponseEntity.ok("account  created successfully");
	}

	@DeleteMapping("/{rib}")
	public ResponseEntity<Object> delete(@PathVariable Long rib) {
		Compte compte = compteRepository.findByRib(rib);
		if (compte == null) {
			return ResponseEntity.status(404).body("Compte with RIB " + rib + " not found.");
		}
		compteRepository.delete(compte);
		return ResponseEntity.ok("compte deleted successfully!");
	}

	@GetMapping("/{rib}")
	public ResponseEntity<Compte> getCompteByRib(@PathVariable Long rib) throws CompteNotFoundException {
		Compte compte = compteRepository.findByRib(rib);
		return ResponseEntity.ok(compte);
	}

	@ResponseBody
	@PostMapping("/delete-ajax/")
	public void deleteAjax(@RequestParam("rib") Integer rib) {
		compteService.delete(rib);
	}

	@PostMapping("/edit")
	public String edit(@RequestParam("rib") Integer rib, Model model) throws CompteNotFoundException {
		Compte compte = compteService.getById(rib);
		model.addAttribute("compte", compte);
		return "edit-compte";
	}

	@PostMapping({ "/update" })
	public String update(@ModelAttribute Compte compte) {
		compteService.saveOrUpdate(compte);
		return "redirect:/comptes/all";
	}

}
