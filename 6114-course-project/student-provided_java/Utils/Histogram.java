package Utils;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;

public class Histogram {
    public static void main(String[] args) {
        ArrayList<Double> values = new ArrayList<Double>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        (new Histogram(values)).Test();
    }

    private ArrayList<Double> values;
    private int[] bins;


    public Histogram(ArrayList<Double> values) {
        this.values = values;
        bins = calculateBins(values);
    }

    public TimeStepPlot generateTimeStepPlot(double timeStep) {
        TimeStepPlot plot = new TimeStepPlot(timeStep, new String[]{"score", "frequency"});
        double binWidth = calculateBinWidth(values);
        double lowestValue = getLowestValue(values, binWidth);

        for (int i = 0; i < bins.length; i ++) {
            plot.addXY(((binWidth * i) + lowestValue), (bins[i] / (double) values.size()));
        }

        plot.saveCSV("ZScore/zscore_");

        return plot;
    }

    private int calculateBinCount(double width, double lowest, 
        double highest) {
        return (int) Math.ceil((highest - lowest) / width);
    }

    private double calculateBinWidth(ArrayList<Double> arr) {
        return 2 * IQR(arr) * Math.pow(arr.size(), -0.33);
    }

    private int median(int l, int r) { 
        int n = r - l + 1; 
        n = (n + 1) / 2 - 1; 
        return n + l;
    }

    // Utilize Freedman–Diaconis rule to determine the bin values
    private double IQR(ArrayList<Double> arr) {
        if (arr.size() < 3) return 1.0;

        Collections.sort(arr);
        // Index of median  
        // of entire data 
        int mid_index = median(0, arr.size()); 

        // Median of first half 
        double q1 = arr.get(median(0, mid_index));

        // Median of second half 
        double q3 = arr.get(median(mid_index + 1, arr.size()));

        // IQR calculation 
        return (q3 - q1);
    } 

    private double getHighestValue(ArrayList<Double> arr) {
        double highest = 0.0;

        for (int i = 0; i < arr.size(); i ++) {
            if (arr.get(i) > highest) {
                highest = arr.get(i);
            }
        }

        return highest;
    }

    private double getLowestValue(ArrayList<Double> arr, double binWidth) {
        double lowest = Double.MAX_VALUE;

        for (int i = 0; i < arr.size(); i ++) {
            if (arr.get(i) < lowest) {
                lowest = arr.get(i);
            }
        }

        return lowest - (binWidth / 4); // jitter the lowest value down a bit
    }

    private int[] calculateBins(ArrayList<Double> data) {

        double binWidth = calculateBinWidth(data);
        int binCount = calculateBinCount(binWidth, getLowestValue(data, binWidth), 
            getHighestValue(data));
        int[] tempBins = new int[binCount];
        double lowestValue = getLowestValue(data, binWidth);

        for (int i = 0; i < binCount; i ++) {
            tempBins[i] = 0;
            for (int j = 0; j < data.size(); j ++) {
                if (doesValueFit(data.get(j), lowestValue + (i * binWidth),
                    lowestValue + ((i + 1) * binWidth))){
                    tempBins[i] ++;
                }
            }
        }

        return tempBins;
    }

    private boolean doesValueFit(double value, double start, double end) {
        return value < end && value > start;
    }

    public void Test() {
        values = new ArrayList<Double>();

        for (int i = 0; i < 1000; i ++) {
            values.add((double)i / 1000);
        }

        assert calculateBinWidth(values) - 0.10232 < 0.001 : "The size of the Bin width is incorrect";
        assert calculateBinCount(calculateBinWidth(values), getLowestValue(values, calculateBinWidth(values)), 
            getHighestValue(values)) == 11 : "The size of the Bin amount is incorrent";

        int[] temp = calculateBins(values);
        int total = 0;
        for (int i = 0; i < temp.length; i++) {
            total += temp[i];
        }

        assert total == values.size() : "The bin count should equal the amount of elements in the data";

        System.out.println("Histogram test cases pass");
    }
}