package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class Compensation extends Employee {
    private static final Logger LOG = LoggerFactory.getLogger(Compensation.class);

    private Integer salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    public Compensation(){ }

    public Compensation(Employee employee, Integer salary, LocalDate effectiveDate) {
        super(employee);
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
