package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.repository.TeamRepository;

@Service
public class PresidentService {
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	
	
	public Iterable<President> getFreePresidents(){
		
		Iterable<President> freePresidents = this.presidentRepository.findAll();
		List<President> usedPresidents = new ArrayList<President>();
		
		Iterable<Team> allTeams = this.teamRepository.findAll();
		Iterable<President> allPresidents = this.presidentRepository.findAll();
		
		for(Team team : allTeams) {
			if(team.getPresident()!=null) {
				usedPresidents.add(team.getPresident());
			}
		}
		
		for(President used : usedPresidents) {
			for (President p : allPresidents) {
				if(used.equals(p)) {
					((List<President>) freePresidents).remove(used);
				}
			}
		}
		
		return freePresidents;
		
	}
	
	

}
