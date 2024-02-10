package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.TeamRepository;

@Service
public class TeamService {
	
	@Autowired
	private TeamRepository teamRepository;
	
	
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
		List<Team> teamWithPresident = this.teamRepository.findByPresidentIsNotNull();
		List<President> usedPresidents = new ArrayList<President>();
		
		for(Team t : teamWithPresident) {
			usedPresidents.add(t.getPresident());
		}
		
		for(President p : usedPresidents) {
			if(p.equals(president)) {
				return true;
			}
		}
		return false;
		
	}
			

	

}
