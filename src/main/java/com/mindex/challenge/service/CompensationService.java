package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {

    Compensation readCompensation(String id);
    Compensation createCompensation(Compensation compensation);
}
