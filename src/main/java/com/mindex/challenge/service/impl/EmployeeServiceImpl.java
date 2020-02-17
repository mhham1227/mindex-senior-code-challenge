package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure readReportingStructure(String id) {
        LOG.debug("Reading employee's [{}] reporting structure", id);

        Integer numberOfReports = 0;
        Employee employee = employeeRepository.findByEmployeeId(id);

        //Check if an employee has any direct reports
        if(employee.getDirectReports() != null){
            //Get the employee information of each direct report
            for(int i =0; i <= employee.getDirectReports().size()-1; i++){
                Employee report = employeeRepository.findByEmployeeId(employee.getDirectReports().get(i).getEmployeeId());
                //Check if a direct report has more direct reports
                if(report.getDirectReports() != null){
                    //Get the reporting structure for the report
                    report = readReportingStructure(report.getEmployeeId());
                    numberOfReports += report.getDirectReports().size();
                }
                //Replace the original direct report record with the completed record
                employee.getDirectReports().set(i, report);
                numberOfReports++;
            }
        }

        return new ReportingStructure(employee, numberOfReports);
    }

    @Override
    public Compensation readCompensation(String id) {
        LOG.debug("Reading employee's [{}] compensation", id);

        Compensation compensation;

        //Only read compensation records
        try{
            compensation = (Compensation) employeeRepository.findByEmployeeId(id);
        }catch (ClassCastException e){
            //Throw exception if there was no compensation data
            throw new RuntimeException("No compensation record for: " + id);
        }

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }

    @Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating employee compensation [{}]", compensation);

        compensation.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(compensation);

        return compensation;
    }

}
