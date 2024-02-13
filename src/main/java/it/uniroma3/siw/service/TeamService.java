package it.uniroma3.siw.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
			

	

}
