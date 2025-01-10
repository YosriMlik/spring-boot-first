package tn.iit.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import tn.iit.dto.StudentDto;
import tn.iit.exception.StudentNotFoundException;
import tn.iit.service.StudentService;

@AllArgsConstructor
@Controller
@RequestMapping("/students") // url d'acces au controlleur
public class StudentController {
	@Autowired
	private  StudentService studentservice; // final sera initialie une seul fois et sera pointe sur la meme
													// instance

	@GetMapping({ "/all" }) // l'url a mettre dans le navigateur pour executer cette methode
	public String findAll(Model model) {
		model.addAttribute("students", studentservice.findAll());
		return "student"; // le nom de fichier html sans extension
	}

	@PostMapping({ "/save" }) // l'url a mettre dans le navigateur pour executer cette methode
	public String save(@RequestParam("id") int id, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		StudentDto studentdto = new StudentDto(id, firstName, lastName);
		studentservice.save(studentdto);
		return "redirect:/students/all"; // le nom de fichier html sans extension
	}

	@GetMapping({ "/delete/{id}" }) // l'url a mettre dans le navigateur pour executer cette methode
	public String delete(@PathVariable("id") int id) {
		studentservice.delete(id);
		return "redirect:/students/all"; // le nom de fichier html sans extension
	}

	@ResponseBody // le controlleur retourne une donnée sous format Json

	@PostMapping("/delete-ajax/")

	public void deleteAjax(@RequestParam("id") int id) {

		studentservice.delete(id);
	}

	@PostMapping({ "/edit" }) // l'url a mettre dans le navigateur pour executer cette methode
	public String edit(@RequestParam("id") int id, Model model) throws StudentNotFoundException {
		Optional<StudentDto> studentopt = studentservice.getById(id);
		if (studentopt.isPresent()) {
			model.addAttribute("student", studentservice.getById(id).get());
		} else {
			throw new StudentNotFoundException(null);
		}
		return "edit-student";
	}

	@PostMapping({ "/update" }) // l'url a mettre dans le navigateur pour executer cette methode
	public String update(@ModelAttribute StudentDto studentDto) {
		studentservice.update(studentDto);
		return "redirect:/students/all";
	}

}
