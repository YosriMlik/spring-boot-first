package tn.iit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.DAO.CompteRepository;
import tn.iit.entite.Compte;
import tn.iit.exception.CompteNotFoundException;

@Service
@RequiredArgsConstructor // Generates a constructor for all final fields
public class CompteService {

    private final CompteRepository compteRepository;

    public void saveOrUpdate(Compte compte) {
        compteRepository.save(compte);
    }

    public List<Compte> findAll() {
        return compteRepository.findAll();
    }

    public void delete(Integer rib) {
        compteRepository.deleteById(rib);
    }

    public Compte getById(Integer rib) throws CompteNotFoundException {
        return compteRepository.findById(rib)
                .orElseThrow(() -> new CompteNotFoundException(rib + " Not Found"));
    }
}