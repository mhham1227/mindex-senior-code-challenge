package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

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
                    ReportingStructure structure  = readReportingStructure(report.getEmployeeId());
                    report = structure.getEmployee();
                    numberOfReports += structure.getNumberOfReports();
                }
                //Replace the original direct report record with the completed record
                employee.getDirectReports().set(i, report);
                numberOfReports++;
            }
        }

        return new ReportingStructure(employee, numberOfReports);
    }

}
