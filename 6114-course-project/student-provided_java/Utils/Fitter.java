package Utils;

import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class Fitter {

    public static void main(String[] args) {
        Test();
    }
	
	public static double[] fit(double x[], double y[], int exponents) {
		
		// Collect data.
		final WeightedObservedPoints obs = new WeightedObservedPoints();
				
		for(int i = 0; i < x.length; i++) {
			obs.add(x[i], y[i]);
		}
		
		// Instantiate a polynomial fitter.
		final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(exponents);

		// Retrieve fitted parameters (coefficients of the polynomial function).
		final double[] coeff = fitter.fit(obs.toList());
		
		return coeff;
	
	}
	
    public static double[] gaussian(double x[], double y[]) {
		
		// Collect data.
		final WeightedObservedPoints obs = new WeightedObservedPoints();
				
		for(int i = 0; i < x.length; i++) {
			obs.add(x[i], y[i]);
		}

		// Retrieve fitted parameters (coefficients of the polynomial function).
		double[] parameters = GaussianCurveFitter.create().fit(obs.toList());
		
		return parameters;
	
	}
	
    public static void Test() {
        assert fit(new double[]{1.0, 2.0}, new double[]{1.0, 2.0}, 1).length > 0 : "Double array should return a array greater than zero";

        double[] linearpath = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
        double[] exponents = fit(linearpath, linearpath, 10);
        
        for (int j = 0; j < linearpath.length; j ++) {
            double x = linearpath[j];
            double y = 0.0;
            for (int i = 0; i < exponents.length; i ++) {
                y += exponents[i] * Math.pow(x, i);
            }

            assert y - x < 0.0000001 : "The x and y values should match relatively equally";
        }

        System.out.println("Fitter test cases pass");
    }
}
