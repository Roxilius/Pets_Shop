package com.example.server.data_transfer_object.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeExpensesResponse {
    private Integer totalExpenses;
    private Integer totalIncome;
}
