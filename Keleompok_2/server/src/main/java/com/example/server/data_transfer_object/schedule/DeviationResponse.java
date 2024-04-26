package com.example.server.data_transfer_object.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviationResponse {
    private Integer totalExpenses;
    private Integer totalIncome;
    private Integer deviation;
    private String conclusion;
}
