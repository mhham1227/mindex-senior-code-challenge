package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeStructureUrl;
    private String employeeCompensationUrl;
    private String employeeCompensationIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeStructureUrl = "http://localhost:" + port + "/employee/structure/{id}";
        employeeCompensationUrl = "http://localhost:" + port + "/employee/compensation/";
        employeeCompensationIdUrl = "http://localhost:" + port + "/employee/compensation/{id}";


    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingStructure(){
        //Check correct number of reports
        /*
        * Reporting Structure for the following test
        *                  Elvis
        *              /            \
        *           Ann              Barb
        *                          /
        *                        Pete
        * */

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Elvis");
        testEmployee.setLastName("Parsley");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Project Lead");

        Employee directReport1 = new Employee();
        directReport1.setFirstName("Ann");
        directReport1.setLastName("Chovey");
        directReport1.setDepartment("Engineering");
        directReport1.setPosition("Analyst");

        Employee directReport2 = new Employee();
        directReport2.setFirstName("Barb");
        directReport2.setLastName("Askew");
        directReport2.setDepartment("Engineering");
        directReport2.setPosition("Developer II");

        Employee directReport2_1 = new Employee();
        directReport2.setFirstName("Pete");
        directReport2.setLastName("Tsar");
        directReport2.setDepartment("Engineering");
        directReport2.setPosition("Developer I");

        Employee createdDirectReport2_1 = restTemplate.postForEntity(employeeUrl, directReport2_1, Employee.class).getBody();

        //Add Direct Reports to test employee
        List<Employee> directReports = new ArrayList<Employee>();
        directReports.add(createdDirectReport2_1);
        directReport2.setDirectReports(directReports);

        //Create Direct Reports
        Employee createdDirectReport1 = restTemplate.postForEntity(employeeUrl, directReport1, Employee.class).getBody();
        Employee createdDirectReport2 = restTemplate.postForEntity(employeeUrl, directReport2, Employee.class).getBody();

        //Add Direct Reports to test employee
        List<Employee> directReports1 = new ArrayList<Employee>();
        directReports1.add(createdDirectReport1);
        directReports1.add(createdDirectReport2);
        testEmployee.setDirectReports(directReports1);

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        assertNotNull(createdDirectReport1.getEmployeeId());
        assertEmployeeEquivalence(directReport1, createdDirectReport1);

        assertNotNull(createdDirectReport2.getEmployeeId());
        assertEmployeeEquivalence(directReport2, directReport2);

        ReportingStructure structure =  restTemplate.getForEntity(employeeStructureUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();

        //Check number of reports
        assertEquals(Integer.valueOf(3), structure.getNumberOfReports());

    }

    @Test
    public void testCompensationReadCreate(){
        Compensation testEmployee = new Compensation();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setSalary(100000);
        testEmployee.setEffectiveDate(LocalDate.of(2020,5,10));

        // Create Compensation checks
        Compensation createdEmployee = restTemplate.postForEntity(employeeCompensationUrl, testEmployee, Compensation.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());
        assertCompensationEquivalence(testEmployee, createdEmployee);

        // Read Compensation checks
        Compensation readEmployee = restTemplate.getForEntity(employeeCompensationIdUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertCompensationEquivalence(createdEmployee, readEmployee);



    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEmployeeEquivalence(expected, actual);

        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());

    }
}
