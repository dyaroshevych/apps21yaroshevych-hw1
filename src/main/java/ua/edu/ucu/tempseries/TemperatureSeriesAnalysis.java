package ua.edu.ucu.tempseries;

import java.util.Arrays;
import java.util.InputMismatchException;

public class TemperatureSeriesAnalysis {
    static final double MIN_TEMPERATURE = -273.0;
    private double[] temperatureSeries;
    private int emptyCellsCount = 0;

    public TemperatureSeriesAnalysis(double[] temperatureSeries) {
        for (double tempValue : temperatureSeries) {
            if (tempValue < MIN_TEMPERATURE) {
                throw new InputMismatchException();
            }
        }
        this.temperatureSeries = temperatureSeries;
    }

    public double sum() {
        double result = 0.0;
        for (double value: temperatureSeries) {
            result += value;
        }
        return result;
    }

    public double average() {
        assertRowNotEmpty();
        return sum() / this.temperatureSeries.length;
    }

    public double deviation() {
        assertRowNotEmpty();

        double variance = 0.0;
        double avg = average();
        for (double tempValue: temperatureSeries) {
            variance += (tempValue - avg) * (tempValue - avg);
        }

        return Math.sqrt(variance / temperatureSeries.length);
    }

    public double min() {
        assertRowNotEmpty();

        double minTemp = temperatureSeries[0];
        for (double tempValue: temperatureSeries) {
            minTemp = Math.min(minTemp, tempValue);
        }

        return minTemp;
    }

    public double max() {
        assertRowNotEmpty();

        double maxTemp = temperatureSeries[0];
        for (double tempValue: temperatureSeries) {
            maxTemp = Math.max(maxTemp, tempValue);
        }

        return maxTemp;
    }

    public double findTempClosestToZero() {
        return findTempClosestToValue(0);
    }


    public double findTempClosestToValue(double value) {
        assertRowNotEmpty();

        double closestTempToValue = temperatureSeries[0];
        for (double tempValue : temperatureSeries) {
            if (Math.abs(tempValue - value) <
                    Math.abs(closestTempToValue - value)) {
                closestTempToValue = tempValue;
            } else if (Math.abs(tempValue - value) ==
                    Math.abs(closestTempToValue - value)) {
                closestTempToValue = Math.max(closestTempToValue, tempValue);
            }
        }
        return closestTempToValue;
    }

    public double[] findTempsLessThen(double value) {
        int countLessThanValue = 0;
        for (double tempValue : temperatureSeries) {
            if (tempValue < value) {
                countLessThanValue += 1;
            }
        }

        double[] result = new double[countLessThanValue];
        int currentIndex = 0;
        for (double tempValue: temperatureSeries) {
            if (tempValue < value) {
                result[currentIndex] = tempValue;
                currentIndex += 1;
            }
        }

        return result;
    }

    public double[] findTempsGreaterThen(double value) {
        int countGreaterThanValue = 0;
        for (double tempValue : temperatureSeries) {
            if (tempValue > value) {
                countGreaterThanValue += 1;
            }
        }

        double[] result = new double[countGreaterThanValue];
        int currentIndex = 0;
        for (double tempValue: temperatureSeries) {
            if (tempValue > value) {
                result[currentIndex] = tempValue;
                currentIndex += 1;
            }
        }

        return result;
    }

    private void assertRowNotEmpty() {
        if (this.temperatureSeries.length == 0) {
            throw new IllegalArgumentException();
        }
    }

    public TempSummaryStatistics summaryStatistics() {
        assertRowNotEmpty();

        return new TempSummaryStatistics(average(), deviation(), min(), max());
    }

    public int addTemps(double... temps) {
        for (double tempValue: temps) {
            increaseStorageIfNeeded();
            for (int idx = 0; idx < temperatureSeries.length; idx++) {
                if (temperatureSeries[idx] == Double.MAX_VALUE) {
                    temperatureSeries[idx] = tempValue;
                    emptyCellsCount -= 1;
                    break;
                }
            }
        }
        return temperatureSeries.length;
    }

    private void increaseStorageIfNeeded() {
        if (emptyCellsCount == 0) {
            temperatureSeries = Arrays.copyOf(temperatureSeries,
                    temperatureSeries.length * 2);
            emptyCellsCount = temperatureSeries.length;
        }
    }
}
