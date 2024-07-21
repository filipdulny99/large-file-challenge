package com.kyotu.largefilereadchallenge.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"com.kyotu.file-path=src/test/resources/temp-measurements.csv"})
@AutoConfigureMockMvc
public class TemperatureControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void batchJob_shouldReturnAverages_whenRequestAndCityFound() throws Exception {
        mockMvc.perform(get("/temperature/batch/{city}", "warszawa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    public void batchJob_shouldReturnNoAverages_whenRequestAndCityNotFound() throws Exception {
        mockMvc.perform(get("/temperature/batch/{city}", "szczecin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void batchJob_shouldReturnAverages_whenCityTooShort() throws Exception {
        mockMvc.perform(get("/temperature/batch/{city}", "a"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void aggregation_shouldReturnAverages_whenRequestAndCityFound() throws Exception {
        mockMvc.perform(get("/temperature/agg/{city}", "warszawa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    public void aggregation_shouldReturnNoAverages_whenRequestAndCityNotFound() throws Exception {
        mockMvc.perform(get("/temperature/agg/{city}", "szczecin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void aggregation_shouldReturnAverages_whenCityTooShort() throws Exception {
        mockMvc.perform(get("/temperature/agg/{city}", "a"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void stream_shouldReturnAverages_whenRequestAndCityFound() throws Exception {
        mockMvc.perform(get("/temperature/funct/{city}", "warszawa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    public void stream_shouldReturnNoAverages_whenRequestAndCityNotFound() throws Exception {
        mockMvc.perform(get("/temperature/funct/{city}", "szczecin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void stream_shouldReturnAverages_whenCityTooShort() throws Exception {
        mockMvc.perform(get("/temperature/funct/{city}", "a"))
                .andExpect(status().isBadRequest());
    }
}
