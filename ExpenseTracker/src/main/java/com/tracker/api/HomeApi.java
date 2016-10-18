package com.tracker.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.domain.users.Role;
import com.tracker.domain.users.User;

@Controller
public class HomeApi {
	
	@ResponseBody
	@RequestMapping( value="/home", method=RequestMethod.GET )
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public User home(@AuthenticationPrincipal User user) {
		System.out.println("Redirecting to HOME");
//		for( Role r : user.getRoles() ){
//			if( r.getName().equals("ROLE_ADMIN"))
//				return "redirect:/admin";
//			}
		User result =  new User.Builder()
				.username( user.getUsername() )
				.email( user.getEmail())
				.id( user.getId())
				.isAdmin(user.isAdmin())
				.roles( user.getRoles())
				.build();
		
			return result;
	}
	

	@RequestMapping( value="/", method=RequestMethod.GET )
	public String welcome(@RequestParam(required=false, defaultValue="false") Boolean error, Model model) {
		model.addAttribute("error", error);
		System.out.println("Returning the login page");
		return "index.html";
	}
	
}
