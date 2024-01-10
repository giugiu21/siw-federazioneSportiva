package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.repository.TeamRepository;

@Service
public class TeamService {
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	
	public void edit(Team team, Long id) {
		Team myTeam = this.teamRepository.findById(id).orElse(null);
	    myTeam.setName(team.getName());
	    myTeam.setFoundingYear(team.getFoundingYear());
	    myTeam.setAddress(team.getAddress());
	    myTeam.setPresident(team.getPresident());
	    myTeam.setPlayers(team.getPlayers());
	    
		teamRepository.save(myTeam);
	}
	
	
	public boolean isUsed(President president) {
		Iterable <President> allPresidents = this.presidentRepository.findAll();
		boolean inUse = false;
		
		for(President p : allPresidents) {
			if(president.equals(p)) {
				inUse = true;
			}
		}
		return inUse;
	}
	
	

}
