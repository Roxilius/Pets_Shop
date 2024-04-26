package com.example.server.data_transfer_object.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DMYResponse {
    private String periode;
    private Integer totalExpenses;
    private Integer totalIncome;
}
