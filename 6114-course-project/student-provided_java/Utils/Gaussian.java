package Utils;

import java.util.ArrayList;

public class Gaussian {
	
	public static ArrayList<Double> gaussianPdf(double[] x, double[] y, double[] gaussian_params){

		ArrayList<Double> gaussian_list  = new ArrayList<Double>();
		double amplitude = gaussian_params[0];
		double mean = gaussian_params[1];
		double std = gaussian_params[2];

		for(int i = 0; i < y.length; i++){
			double result;
			result = amplitude * Math.exp(-0.5 * Math.pow(((x[i] - mean)/std), 2));
			gaussian_list.add(result);
		}

		return gaussian_list;

	}

}
