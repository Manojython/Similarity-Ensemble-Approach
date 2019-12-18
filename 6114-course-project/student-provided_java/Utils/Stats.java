package Utils;

import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;

public class Stats
{
    public static void main(String args[])
    {
        Test();
    }

    public static double Mean(double[] input) 
    {
        if (input.length == 0) return 0.0;
        if (input.length == 1) return input[0];

        // calculate mean
        double sum = 0; 
        for (int i = 0; i < input.length; i ++) {
            sum += input[i];
        }

        return sum / input.length;
    }

    public static double StandardDeviation(double[] input, double mean)
    {
        if (input.length == 0) return 0.0;
        if (input.length == 1) return input[0];

        // calculate std
        double summation = 0.0;
        for (int i = 0; i < input.length; i ++) {
            summation += (input[i] - mean) * (input[i] - mean);
        }

        return Math.sqrt(summation / (input.length - 1.0));
    }

    public static void Test() 
    {
        double[] tempEmpty = new double[0];
        assert StandardDeviation(tempEmpty, Mean(tempEmpty)) == 0.0 : "Edge cases, such as a empty array, should be ignored";

        double[] tempSingle = {1.0};
        assert StandardDeviation(tempSingle, Mean(tempSingle)) == 1.0 : "Edge cases, such as a single sized array, should be ignored";

        double[] tempZero = {0.0, 0.0, 0.0};
        assert StandardDeviation(tempZero, Mean(tempZero)) == 0.0 : "Edge cases, such as a zero array, should be ignored";

        double[] tempOther = {1, 2, 3, 4, 5, 6, 7, 8 ,9, 10};
        assert StandardDeviation(tempOther, Mean(tempOther)) == 3.0276503540974917 : "Standard Deviation is not calcualated correctly";

        System.out.println("Stats test cases pass");
    }
}