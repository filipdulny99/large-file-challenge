package com.kyotu.largefilereadchallenge.model.data.calculation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Total {
    private double sum;
    private int count;

    public Total(double sum) {
        this.sum = sum;
        this.count = 1;
    }

    public double getAverage() {
        return this.sum / this.count;
    }

    public Total getUpdatedTotal(double value) {
        this.sum += value;
        this.count++;
        return this;
    }
}
