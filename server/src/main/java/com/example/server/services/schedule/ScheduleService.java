package com.example.server.services.schedule;

import java.time.LocalDate;

import com.example.server.data_transfer_object.schedule.DMYResponse;
import com.example.server.data_transfer_object.schedule.DeviationResponse;
import com.example.server.data_transfer_object.schedule.IncomeExpensesResponse;

public interface ScheduleService {
    DMYResponse daily();
    DMYResponse monthly();
    DMYResponse annual();
    DeviationResponse deviation(LocalDate startDate, LocalDate endDate);
    IncomeExpensesResponse incomeExpenses(LocalDate startDate, LocalDate endDate);
}
