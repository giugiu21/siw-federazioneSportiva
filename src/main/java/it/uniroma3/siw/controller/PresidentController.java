package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.UserService;

@Controller
public class PresidentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PlayerService playerService;
	
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	
	
	
	@GetMapping("/president/myTeam")
	public String showMyTeam(Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		
		if(president!=null){
			model.addAttribute("president", true);
			
			/*Trovo il team del presidente*/
			
			Team myTeam = this.teamRepository.findByPresident(president);
			
			if(myTeam!=null) {
				model.addAttribute("myTeam", myTeam);
				
				this.playerService.freePlayersFromContract();
				
				Iterable<Player> freePlayers = this.playerService.getFreePlayers();
				model.addAttribute("players", freePlayers);
				model.addAttribute("player", new Player());
				
				return "president/myTeam.html";
			}
			else {
				model.addAttribute("noTeam", true);
				return "president/myTeam.html";
			}
		}
		return "index.html";
	}
	
	@PostMapping("/president/newPlayer")
	public String addPlayer(@ModelAttribute("player") Player player, Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		
		if(president!=null){
			model.addAttribute("president", true);
			
			/*Trovo il team del presidente*/
			Team myTeam = this.teamRepository.findByPresident(president);
			
			if(myTeam!=null) {
				model.addAttribute("myTeam", myTeam);
				
				this.playerRepository.save(player);
				
				myTeam.getPlayers().add(player);
				this.teamRepository.save(myTeam);
				
				this.playerService.freePlayersFromContract();
				
				Iterable<Player> freePlayers = this.playerService.getFreePlayers();
				model.addAttribute("players", freePlayers);
				model.addAttribute("player", new Player());
				
				return "president/myTeam.html";
			}
			else {
				return "president/myTeam.html";
			}
		}
		return "index.html";
		
		
	}
	
	@GetMapping("/president/addPlayer/{playerId}")
	public String addPlayers(@PathVariable("playerId") Long playerId,  Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		if(president!=null){
			model.addAttribute("president", true);
		}
		
		
		Player player = this.playerRepository.findById(playerId).get();
		
		
//		/*metto automaticamente la data di tesseramento*/
//		LocalDate now = LocalDate.now();
//		player.setStartDate(now);
//		LocalDate oneYearAfter = now.plusYears(1);
//		player.setEndDate(oneYearAfter);
//		
//		team.getPlayers().add(player);//Aggiungo il giocatore alla squadra
//		this.teamRepository.save(team);
		
		
//		this.playerService.freePlayersFromContract();
//		
//		Iterable<Player> freePlayers = this.playerService.getFreePlayers();
//		model.addAttribute("players", freePlayers);
//		model.addAttribute("myTeam", team);
//		model.addAttribute("player", new Player());
		
		model.addAttribute("player", player);
		
		
		return "president/addPlayerToTeam.html";
	}
	
	@PostMapping("/president/addPlayer/{id}")
	public String addPlayer(@ModelAttribute("player") Player player, @PathVariable("id") Long playerId, Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		if(president!=null){
			model.addAttribute("president", true);
		}
		
		Team myTeam = this.teamRepository.findByPresident(president);
		
		
		this.playerService.edit(player, playerId);
		myTeam.getPlayers().add(this.playerRepository.findById(playerId).orElse(null));
		this.playerService.freePlayersFromContract();
		
		Iterable<Player> freePlayers = this.playerService.getFreePlayers();
		model.addAttribute("players", freePlayers);
		model.addAttribute("myTeam", myTeam);
		model.addAttribute("player", new Player());
		
		return "president/myTeam.html";
		
		
	}
	
	@GetMapping("/president/deletePlayer/{teamId}/{playerId}")
	public String removePlayer(@PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId,  Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		if(president!=null){
			model.addAttribute("president", true);
		}
		
		Player player = this.playerRepository.findById(playerId).get();
		
		
		Team team = this.teamRepository.findById(teamId).get();
		
		team.getPlayers().remove(player);
		player.setStartDate(null);
		player.setEndDate(null);
		
		this.teamRepository.save(team);
		this.playerRepository.save(player);
		
		
		this.playerService.freePlayersFromContract();
		
		Iterable<Player> freePlayers = this.playerService.getFreePlayers();
		model.addAttribute("players", freePlayers);
		model.addAttribute("myTeam", team);
		model.addAttribute("player", new Player());
		
		
		return "president/myTeam.html";
	}
	

}
