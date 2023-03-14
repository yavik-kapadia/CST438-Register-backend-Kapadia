package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.StudentController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

/* 
 * Example of using Junit with Mockito for mock objects
 *  the database repositories are mocked with test data.
 *  
 * Mockmvc is used to test a simulated REST call to the RestController
 * 
 * the http response and repository is verified.
 * 
 *   Note: This tests uses Junit 5.
 *  ContextConfiguration identifies the controller class to be tested
 *  addFilters=false turns off security.  (I could not get security to work in test environment.)
 *  WebMvcTest is needed for test environment to create Repository classes.
 */
@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {

	static final String URL = "http://localhost:8080";
	   

	public static final String TEST_STUDENT_EMAIL = "test11@csumb.edu";
    public static final String TEST_STUDENT_NAME  = "test11";


	@MockBean
	CourseRepository courseRepository;

	@MockBean
	StudentRepository studentRepository;

	@MockBean
	EnrollmentRepository enrollmentRepository;

	@MockBean
	GradebookService gradebookService;
	
		

	@Autowired
	private MockMvc mvc;

	@Test
	@DirtiesContext
	public void addStudent()  throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .param("name", TEST_STUDENT_NAME)
			      .param("email", TEST_STUDENT_EMAIL)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus()); 
		
		boolean found = false;
		Student response_student = fromJsonString(response.getContentAsString(), Student.class);
		if(response_student.getEmail().equals(TEST_STUDENT_EMAIL) && 
				response_student.getName().equals(TEST_STUDENT_NAME))
		{
			found = true;
		}
			
		assertEquals(true, found);
		
		verify(studentRepository).save(any(Student.class));
		System.out.println(student);
		verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL);
		
		
	}
	@Test
	@DirtiesContext
	public void addHold()  throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		

		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .param("name", TEST_STUDENT_NAME)
			      .param("email", TEST_STUDENT_EMAIL)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus()); 
		
		boolean found = false;
		Student response_student = fromJsonString(response.getContentAsString(), Student.class);
		if(response_student.getEmail().equals(TEST_STUDENT_EMAIL) && 
				response_student.getName().equals(TEST_STUDENT_NAME))
			found = true;
		
		assertEquals(true, found);
		
		verify(studentRepository).save(any(Student.class));
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
	    response = mvc.perform(
	              MockMvcRequestBuilders
	                 .post("/student/addhold")
	                 .param("email", TEST_STUDENT_EMAIL)
	                 .accept(MediaType.APPLICATION_JSON))
	              .andReturn().getResponse();
	      // verify that return status = OK (value 200) 
	      assertEquals(200, response.getStatus());
	      
	      Boolean add_hold =  false;
	      
	      Student hold_student = fromJsonString(response.getContentAsString(), Student.class);
	      
	      if(hold_student.getStatusCode() == 1)
	    	  add_hold = true;
	      
	      assertEquals(true, add_hold);
	      
	      verify(studentRepository, times(2)).findByEmail(TEST_STUDENT_EMAIL);
		
	}


	@Test
	@DirtiesContext
	public void removeHold()  throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .param("name", TEST_STUDENT_NAME)
			      .param("email", TEST_STUDENT_EMAIL)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus()); 
		
		boolean found = false;
		Student response_student = fromJsonString(response.getContentAsString(), Student.class);
		if(response_student.getEmail().equals(TEST_STUDENT_EMAIL) && 
				response_student.getName().equals(TEST_STUDENT_NAME))
			found = true;
		
		assertEquals(true, found);
		
		verify(studentRepository).save(any(Student.class));
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		// add hold
	      response = mvc.perform(
	              MockMvcRequestBuilders
	                 .post("/student/addhold")
	                 .param("email", TEST_STUDENT_EMAIL)
	                 .accept(MediaType.APPLICATION_JSON))
	              .andReturn().getResponse();
	      // verify that return status = OK (value 200) 
	      assertEquals(200, response.getStatus());
	      
	      Boolean add_hold =  false;
	      
	      Student hold_student = fromJsonString(response.getContentAsString(), Student.class);
	      
	      if(hold_student.getStatusCode() == 1)
	    	  add_hold = true;
	      
	      assertEquals(true, add_hold);
	      
	   // remove hold
	      response = mvc.perform(
	              MockMvcRequestBuilders
	                 .post("/student/removehold")
	                 .param("email", TEST_STUDENT_EMAIL)
	                 .accept(MediaType.APPLICATION_JSON))
	              .andReturn().getResponse();
	      // verify that return status = OK (value 200) 
	      assertEquals(200, response.getStatus());
	      
	      
	      Boolean removed_hold = false;
	      // verify hold has been removed
	      Student no_hold_student = fromJsonString(response.getContentAsString(), Student.class);
	      if(no_hold_student.getStatusCode() == 0)
	    	  removed_hold = true;
	      assertEquals(true, removed_hold);
	      
	      verify(studentRepository, times(3)).findByEmail(TEST_STUDENT_EMAIL);
	      

	}


		
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
