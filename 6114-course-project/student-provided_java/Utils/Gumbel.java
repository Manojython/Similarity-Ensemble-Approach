package Utils;

import java.util.ArrayList;
import java.util.List;

public class Gumbel{
	
	public static ArrayList<Double> gumbelPdf(double[] array){

		ArrayList<Double> gumble_list  = new ArrayList<Double>();

		for(double x : array) {
			double calc;
			calc = Math.exp(-(x + Math.exp(-x)));
			gumble_list.add(calc);
		}

		return gumble_list;
	}
}