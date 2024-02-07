package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validators.CredentialsValidator;
import it.uniroma3.siw.validators.UserValidator;
import jakarta.validation.Valid;

@Controller
public class ViewController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	
	
	
	@GetMapping("/")
	public String getIndex(Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = null;
		Credentials credentials = null;
		if(!(authentication instanceof AnonymousAuthenticationToken)){
			userDetails = (UserDetails)authentication.getPrincipal();
			credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		}
		
		if(userDetails != null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		
		/*Controllo se è presidente*/
		if(credentials!=null) {
			President president = this.presidentRepository.findByUsername(userDetails.getUsername());
			if(president!=null){
				model.addAttribute("president", true);
			}
			
		}
		
		return "index.html";
	}
	
	@GetMapping("/success")
	public String loginSuccess(Model model) {
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
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		if(president!=null){
			model.addAttribute("president", true);
		}

		model.addAttribute("userDetails", userDetails);
		return "index.html";
	}
	
	
	@GetMapping("/register")
	public String registration(Model model){
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegistration.html";
		
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult userBindingResult, @Valid
			@ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult,
			Model model) {
		this.userValidator.validate(user,userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);                        
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			userService.saveUser(user);
			model.addAttribute("user", user);
			return "formLogin.html";
		}
		else {
			if(userBindingResult.hasErrors()) {
				model.addAttribute("registrationErrorUser", "*Email già in uso");
			}
			model.addAttribute("registrationError", "*Username già in uso");
		}
		return "formRegistration.html";
	}
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		return "formLogin.html";
	}
	
	
	@GetMapping("/allTeams")
	public String allTeams(Model model) {
		UserDetails userDetails = this.userService.getUserDetails();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		
		if(userDetails!=null) {
			model.addAttribute("userDetails", userDetails);
		}
		if(credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("admin", true);
		}
		/*Controllo se è presidente*/
		President president = this.presidentRepository.findByUsername(userDetails.getUsername());
		if(president!=null){
			model.addAttribute("president", true);
		}
		
		this.playerService.freePlayersFromContract();
		model.addAttribute("teams", this.teamRepository.findAll());
		return "allTeams.html";
	}
	
	

}
