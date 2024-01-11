package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.Team;

@Service
public class PlayerService {
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	
	public void freePlayersFromContract() {
		Iterable<Team> allTeams = this.teamRepository.findAll();
		
		for(Team team : allTeams) {
			for(Player usedPlayer : team.getPlayers()) {
				if(usedPlayer.getEndDate().equals(java.time.LocalDate.now())) {
					team.getPlayers().remove(usedPlayer);
					usedPlayer.setStartDate(null);
					usedPlayer.setEndDate(null);
					this.playerRepository.save(usedPlayer);
				}
			}
		}
	}
	
	
	public Iterable<Player> getFreePlayers(){
		Iterable <Player> freePlayers = this.playerRepository.findAll();
		List<Player> usedPlayers = new ArrayList<Player>();
		
		Iterable<Player> allPlayers = this.playerRepository.findAll();
		Iterable<Team> allTeams = this.teamRepository.findAll();
		
		for(Team team : allTeams) {
			if(team.getPlayers()!=null) {
				for(Player playerTeam : team.getPlayers()) {
					usedPlayers.add(playerTeam);
				}
			}
		}
		
		for(Player used : usedPlayers) {
			for (Player p : allPlayers) {
				if(used.equals(p)) {
					((List<Player>) freePlayers).remove(used);
				}
			}
		}
		
		
		return freePlayers;
	}

}
