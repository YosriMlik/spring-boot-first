package tn.iit.service;



import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;



import tn.iit.dto.StudentDto;



@Service

public class StudentService {



	private List<StudentDto> students = new ArrayList<>();



	public StudentService() {

		students.add(new StudentDto(1, "Mariam", "Mseddi"));

	}



	public void save(StudentDto studentDto) {

		students.add(studentDto);

	}



	public List<StudentDto> findAll() {

		return students;

	}
	
	public void delete(int id ) {

		students.removeIf(s->s.getId()==id);

	}



	public Optional <StudentDto >getById(int id) {
		return students.stream().filter(s->s.getId()==id).findFirst();
		 
	}
	public void update(StudentDto studentDto) {
		students.set(students.indexOf(studentDto),studentDto);
	}
	
	



}

