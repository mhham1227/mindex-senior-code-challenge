package com.mindex.challenge.data;

public class ReportingStructure extends Employee {

    private Integer numberOfReports;

    public ReportingStructure(){
    }
    public ReportingStructure(Employee employee, Integer numberOfReports) {
        super(employee);
        this.numberOfReports = numberOfReports;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(Integer numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
