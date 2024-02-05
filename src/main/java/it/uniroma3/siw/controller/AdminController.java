package it.uniroma3.siw.controller;



import org.springframework.beans.factory.annotation.Autowired;
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
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.repository.PresidentRepository;

@Controller
public class AdminController {
	
	@Autowired
	private TeamRepository teamRepository;
	

	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PresidentService presidentService;

	@Autowired
	private TeamService teamService;


	
	@RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.GET)
	public String deleteTeam(@PathVariable("id") Long id, Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
		this.teamRepository.deleteById(id);
		model.addAttribute("teams", this.teamRepository.findAll());
		return "allTeams.html";
	}

	
	
	@GetMapping("/admin/formNewTeam")
	public String formNewTeam(Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
		model.addAttribute("team", new Team());
		model.addAttribute("presidents", this.presidentService.getFreePresidents());
		return "admin/formNewTeam.html";
	}
	
	@PostMapping("/admin/newTeam")
	public String newTeam(@ModelAttribute("team") Team team, Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
		if (!teamRepository.existsByName(team.getName())) {
			this.teamRepository.save(team);
			model.addAttribute("teams", this.teamRepository.findAll());
			
			return "allTeams.html";
			
		} else {
			model.addAttribute("newTeamError", "Questa squadra già esiste!");
			return "admin/formNewTeam.html";
		}
	}
	
	
	@GetMapping("/admin/formEditTeam/{id}")  
    public String formEditTeam(@PathVariable("id") Long id, Model model){

		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
        model.addAttribute("team", this.teamRepository.findById(id).get());
        model.addAttribute("presidents", this.presidentRepository.findAll());
        return "admin/formEditTeam.html";
    }
	
	
	
	@PostMapping("/admin/formEditTeam/{id}")
	public String editTeam(@ModelAttribute("team") Team team, @PathVariable("id") Long teamId,  Model model) {
		
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
		boolean pInUse = this.teamService.isUsed(team.getPresident());
		
		
		if(pInUse) {
			model.addAttribute("team", this.teamRepository.findById(teamId).get());
			model.addAttribute("PresidentError", "Presidente già in uso, scegline un altro");
			model.addAttribute("presidents", this.presidentRepository.findAll());
			return "admin/formEditTeam.html";
		}
		
		else {
			this.teamService.edit(team, teamId);
			model.addAttribute("teams", this.teamRepository.findAll());
			
			return "allTeams.html";
		}
		
		
	}

	


	
	
}
