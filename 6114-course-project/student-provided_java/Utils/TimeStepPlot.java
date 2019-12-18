package Utils;

import java.util.ArrayList;

public class TimeStepPlot {

    public static void main(String[] args) {
        (new TimeStepPlot(0.01, new String[]{"hello", "world"})).Test();
    }

    public static final int EXPONENT_COUNT = 10;

    private double timeStep;
    private String[] header;
    private ArrayList<Point> points;
    private double[] exponentConstants;

    public TimeStepPlot(double timeStep, String[] header) {
        this.timeStep = timeStep;
        this.header = header;

        points = new ArrayList<Point>();
    }

    public void addXY(double x, double y) {
        points.add(new Point(x,y));
    }

    public double[][] getXY() {
    	double[][] values = new double[points.size()][2];
    	
    	for (int i = 0; i < points.size(); i ++) {
            values[i][0] = points.get(i).getX();
            values[i][1] = points.get(i).getY();
        }

    	return values;
    }

    public double getMaxX() {
        double max = 0;

        for (int j = 0; j < points.size(); j ++) {
            if (points.get(j).getX() > max) {
                max = points.get(j).getX();
            }
        }

        return max;
    }

    /*
        Returns all Y values thar correspond to a X value in the plot
     */
    public double[] getYArray(double x) {
        ArrayList<Double> yValues = new ArrayList<Double>();

        for (int k = 0; k < points.size(); k ++) {
            if (points.get(k).getX() == x) {
                yValues.add(points.get(k).getY());
            }
        }

        double[] list = new double[yValues.size()];
        for (int l = 0; l < list.length; l ++) {
            list[l] = yValues.get(l);
        }

        return list;
    }

    public double getYValue(double x) {
        if (exponentConstants == null) reflect();

        double y = 0.0;

        for (int i = 0; i < exponentConstants.length; i ++) {
            y += exponentConstants[i] * Math.pow(x, i);
        }

        return y;
    }

    private void reflect() {
        double[] xValues = new double[points.size()];
        double[] yValues = new double[points.size()];

        for (int i = 0; i < points.size(); i ++) {
            xValues[i] = points.get(i).getX();
            yValues[i] = points.get(i).getY();
        }

        exponentConstants = Fitter.fit(xValues, yValues, EXPONENT_COUNT);
    }

    public void printExponentConstants() {
        if (exponentConstants == null) reflect();

        System.out.print("y = ");

        for (int i = exponentConstants.length - 1; i >= 0; i --) {

            if (exponentConstants[i] > 0.0) {
                if (i < exponentConstants.length - 1) 
                    System.out.print(" + ");

                if (i == 0) {
                    System.out.print(exponentConstants[i]);
                } else {
                    System.out.print(exponentConstants[i] + "(x^" + i + ")");   
                }
            }
        }

        System.out.println();
    }

    public void saveCSV(String path) {
        TableXY.write(path + timeStep + ".csv", header, points);
    }

    public void Test() {
        // add a straight line
        double[] linearpath = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
        points.clear();
        for (int i = 0; i < linearpath.length; i ++) {
            addXY(linearpath[i], linearpath[i]);
        }

        for (int j = 0; j < linearpath.length; j ++) {
            double x = linearpath[j];
            double y = getYValue(x);

            assert y - x < 0.0000001 : "The x and y values should match relatively equally";
        }

        System.out.println("TimeStepPlot test cases pass");
    }
}