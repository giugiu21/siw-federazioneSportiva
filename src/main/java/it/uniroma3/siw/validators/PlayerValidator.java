package it.uniroma3.siw.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import it.uniroma3.siw.model.Player;

@Component
public class PlayerValidator implements Validator{

	
	@Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        if(player.getStartDate() == null && player.getEndDate() == null){
            errors.reject("player.date.notValid");
        }
    }
    
   
}
