package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		//TODO  complete this method in homework 4
		// Create Enrollment DTO
		EnrollmentDTO enrollDTO = new EnrollmentDTO(student_email, student_name, course_id);
		
		restTemplate.postForEntity(gradebook_url+"/enrollment", enrollDTO, EnrollmentDTO.class);
		
		
	}

}
