package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {

	@Autowired
	StudentRepository studentRepository;

	
	/*
	 * Add student endpoint
	 */
	@PostMapping("/student")
	@Transactional
	public Student addStudent( @RequestParam("name") String name, @RequestParam("email") String email) {
		System.out.println("/Student called.");
		
		Student student = studentRepository.findByEmail(email);
		if(student == null) {
			student = new Student();
			student.setEmail(email);
			student.setName(name);
			studentRepository.save(student);
			return student;
		}
		
		else {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST,"Student already exists");
			
		}
	}
	
	/*
	 * Add hold  
	 */
	@PostMapping("/student/addhold")
	@Transactional
	public Student addHold(@RequestParam("email") String email) {

		Student student = studentRepository.findByEmail(email);
		
		if(student != null) {
			student.setStatus("HOLD");
			student.setStatusCode(1);
			studentRepository.save(student);
			return student;
		}
		else {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST,"No student with this information was found.");
		}
	}
	
	

	/*
	 * Remove hold  
	 */
	@PostMapping("/student/removehold")
	@Transactional
	public Student removeHold(@RequestParam("email") String email) {

		Student student = studentRepository.findByEmail(email);
		
		if(student != null) {
			student.setStatus(null);
			student.setStatusCode(0);
			studentRepository.save(student);
			return student;
		}
		else {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST,"No student with this information was found.");
		}
	}
	
	
}
