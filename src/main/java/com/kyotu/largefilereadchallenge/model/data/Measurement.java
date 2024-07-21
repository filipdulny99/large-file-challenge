package com.kyotu.largefilereadchallenge.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    private String city;
    private String measuredAt;
    private Double temperature;
}
