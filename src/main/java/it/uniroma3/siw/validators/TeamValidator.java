package it.uniroma3.siw.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.TeamRepository;



@Component
public class TeamValidator implements Validator{

	@Autowired
    private TeamRepository teamRepository;
	
	@Override
    public boolean supports(Class<?> clazz) {
        return Team.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Team team = (Team) target;
        if(team.getName()==null || (team.getName() != null && this.teamRepository.existsByName(team.getName()))){
            errors.reject("team.name.duplicate");
        }
    }
    

}
