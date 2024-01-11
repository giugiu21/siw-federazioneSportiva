package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;

@Controller
public class PresidentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PlayerService playerService;
	
	
	@Autowired
	private PresidentService presidentService;
	
	
	
	@GetMapping("/president/myTeam")
	public String showMyTeam(Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		User user =  this.userRepository.findById(credentials.getUser().getId()).orElse(null);
		
		if(user!=null && this.teamService.isPresident(user.getName(), user.getLastname())){
			
			President president = this.presidentService.getPresident(user.getName(), user.getLastname());
			model.addAttribute("president", true);
			
			/*Trovo il team del presidente*/
			Team myTeam = this.teamService.getMyTeam(president);
			
			if(myTeam!=null) {
				model.addAttribute("myTeam", myTeam);
				
				this.playerService.freePlayersFromContract();
				
				Iterable<Player> freePlayers = this.playerService.getFreePlayers();
				model.addAttribute("players", freePlayers);
				
				return "president/myTeam.html";
			}
			else {
				model.addAttribute("noTeam", true);
				return "president/myTeam.html";
			}
		}
		return "index.html";
		//return "president/myTeam.html";
	}
	
	@GetMapping("/president/addPlayers/{teamId}/{playerId}")
	public String addPlayers(@PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId,Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		User user =  this.userRepository.findById(credentials.getUser().getId()).orElse(null);
		
		if(user!=null && this.teamService.isPresident(user.getName(), user.getLastname())){
			model.addAttribute("president", true);
		}
		
		Player player = this.playerRepository.findById(playerId).get();
		Team team = this.teamRepository.findById(teamId).get();
		/*metto automaticamente la data di tesseramento*/
		LocalDate now = LocalDate.now();
		player.setStartDate(now);
		LocalDate oneYearAfter = now.plusYears(1);
		player.setEndDate(oneYearAfter);
		
		team.getPlayers().add(player);//Aggiungo il giocatore alla squadra
		this.teamRepository.save(team);
		
		
		this.playerService.freePlayersFromContract();
		
		Iterable<Player> freePlayers = this.playerService.getFreePlayers();
		model.addAttribute("players", freePlayers);
		model.addAttribute("myTeam", team);
		
		
		return "president/myTeam.html";
	}
	

}
