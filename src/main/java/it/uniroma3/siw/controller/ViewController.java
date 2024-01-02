package it.uniroma3.siw.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ViewController {
	
	@GetMapping("/")
	public String getIndex(Model model){
		return "index.html";
	}

}
