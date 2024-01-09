package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.repository.PresidentRepository;

@Controller
public class AdminController {
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired
	private CredentialsService credentialsService;

	
	@RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.GET)
	public String deleteRecipe(@PathVariable("id") Long id, Model model) {
		this.teamRepository.deleteById(id);
		model.addAttribute("messaggioCancellazione", "Squadra cancellata");
		model.addAttribute("teams", this.teamRepository.findAll());
		model.addAttribute("admin", true);
		return "recipes.html";
	}

	
	@GetMapping("/admin/formEditTeam/{id}")  
    public String formEditTeam(@PathVariable("id") Long id, Model model){
        model.addAttribute("team", this.teamRepository.findById(id).get());
        model.addAttribute("players", this.playerRepository.findAll());
        return "admin/formEditTeam.html";
    }
	
	@GetMapping("admin/formNewTeam")
	public String formNewTeam(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = null;
		Credentials credentials = null;
		if(!(authentication instanceof AnonymousAuthenticationToken)){
			userDetails = (UserDetails)authentication.getPrincipal();
			credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		}
		
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}

		model.addAttribute("userDetails", userDetails);
		model.addAttribute("team", new Team());
		model.addAttribute("presidents", presidentRepository.findAll());
		return "admin/formNewTeam.html";
	}
	
	@PostMapping("admin/newTeam")
	public String newTeam(@ModelAttribute("team") Team team, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = null;
		Credentials credentials = null;
		if(!(authentication instanceof AnonymousAuthenticationToken)){
			userDetails = (UserDetails)authentication.getPrincipal();
			credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		}
		
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}

		model.addAttribute("userDetails", userDetails);
		if (!teamRepository.existsByName(team.getName())) {
			this.teamRepository.save(team);
			model.addAttribute("team", team);
			
			return "allTeams.html";
			
		} else {
			model.addAttribute("newTeamError", "Questa squadra già esiste!");
			return "admin/formNewTeam.html";
		}
	}
}
