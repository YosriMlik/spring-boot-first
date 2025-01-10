package tn.iit.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/hello") //url d'acces au controlleur
public class HelloController {
	@GetMapping("/home") //l'url a mettre dans le navigateur pour executer cette methode
	public String goToHello( Model model) {
		model.addAttribute("date",LocalTime.now());
		return "hello"; //le nom de fichier html sans extension
	}
	
@GetMapping({"home2"}) //l'url a mettre dans le navigateur pour acceder a la page 
public ModelAndView gotoHello2() {
    ModelAndView modelandview=new ModelAndView();
    modelandview.setViewName("hello");//le nom du fichier html sans extensions
    modelandview.addObject("date",LocalDate.now());
    return modelandview;
}


}
