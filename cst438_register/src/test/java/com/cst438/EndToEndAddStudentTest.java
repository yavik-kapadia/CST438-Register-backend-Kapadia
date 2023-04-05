package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Student;
/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndAddStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/usr/bin/chromedriver";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "Jim.bob@csumb.edu";

	public static final String TEST_USER_NAME = "Jim Bob";
	// public static final int TEST_COURSE_ID = 40443; 

	// public static final String TEST_SEMESTER = "2021 Fall";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	StudentRepository studentRepository;


	/*
	 * Admin add a new student test
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * if student is already exists, then delete the student.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// select the last of the radio buttons on the list of semesters page.
			
			WebElement we = driver.findElement(By.xpath("(//input[@type='radio'])[last()]"));
			we.click();

			// Locate and click "Add student" button
			
			driver.findElement(By.xpath("//button[@id='open_add_dialog']")).click();
			Thread.sleep(SLEEP_DURATION);


			// enter student name and click Add button
			
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
	
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			assertEquals("Student added", toast_text);
			
			Student stu = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertEquals(TEST_USER_NAME, stu.getName());
			
		} catch (Exception ex) {
			throw ex;
		} finally {



			driver.quit();
		}

	}
}
