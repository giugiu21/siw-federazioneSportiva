package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.repository.PresidentRepository;

@Service
public class PresidentService {
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	public President getPresident(String name, String lastname) {
		List<President> presidents = this.presidentRepository.findByNameAndLastname(name, lastname);
		President president = null;
		for(President p : presidents) {
			if(p!=null) {
				president = this.presidentRepository.findById(p.getId()).orElse(null);
			}
		}
		return president;
	}

}
