package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    CompensationService compensationService;

    @GetMapping("/employee/compensation/{id}")
    public Compensation readCompensation(@PathVariable("id")  String id){
        LOG.debug("Received compensation info request for [{}]", id);

        return compensationService.readCompensation(id);
    }

    @PostMapping("/employee/compensation")
    public Compensation createCompensationRequest(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation info create request for [{}]", compensation.getEmployee().getEmployeeId());

        return compensationService.createCompensation(compensation);
    }
}
