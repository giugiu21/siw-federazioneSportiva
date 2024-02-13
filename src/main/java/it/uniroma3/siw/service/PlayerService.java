package it.uniroma3.siw.service;

import java.util.List;
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
	
	
	public void edit(Player player, Long id) {
		Player playerEdited = this.playerRepository.findById(id).orElse(null);
		
		playerEdited.setStartDate(player.getStartDate());
		playerEdited.setEndDate(player.getEndDate());
		
		this.playerRepository.save(playerEdited);
	}
	
	
	public void freePlayersFromContract() {		
		List<Player> playersToFree = this.playerRepository.findAllByEndDateBefore(java.time.LocalDate.now());
		
		for(Player p :  playersToFree) {
			Team t  = this.teamRepository.findByPlayer(p.getId());
			t.getPlayers().remove(p);
			p.setStartDate(null);
			p.setEndDate(null);
			this.teamRepository.save(t);
			this.playerRepository.save(p);
		}
		
	}


}
