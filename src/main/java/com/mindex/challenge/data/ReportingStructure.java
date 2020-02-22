package com.mindex.challenge.data;

public class ReportingStructure {

    private Integer numberOfReports;
    private Employee employee;

    public ReportingStructure(){
    }
    public ReportingStructure(Employee employee, Integer numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(Integer numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
