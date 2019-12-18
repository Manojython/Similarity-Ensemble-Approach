import java.util.Random;
import java.util.ArrayList;

public class SampleProductFactors
{
    public static void main(String args[])
    {
        (new SampleProductFactors(10, 1, 1)).Test();
    }

    private int sampleProduct;
    private ArrayList<Integer> factors;

    public SampleProductFactors(int sampleProduct, int smin, int smax) {
        this.sampleProduct = sampleProduct;
        this.factors = getSampleProductFactors(sampleProduct, smin, smax);
    }

    public int getSampleProduct() {
        return sampleProduct;
    }

    public ArrayList<Integer> getFactors() {
        return factors;
    }

    private ArrayList<Integer> getFactors(int number) {
        ArrayList<Integer> factors = new ArrayList<Integer>();

        for (int i = 2; i <= number; i ++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }

        return factors;
    }

    private ArrayList<Integer> getSampleProductFactors(int sampleProduct, 
        int smin, int smax) {
        ArrayList<Integer> completeFactors = getFactors(sampleProduct);
        ArrayList<Integer> factors = new ArrayList<Integer>();

        for (int i = 0; i < completeFactors.size(); i ++) {
            if (completeFactors.get(i) >= smin && completeFactors.get(i) <= smax) {
                factors.add(completeFactors.get(i));
            }
        }

        return factors;
    }

    public void Test() 
    {
        int[] expectedFactors = {2, 3, 4, 6, 8, 12, 16, 24, 48};
        ArrayList<Integer> tempFactors = getFactors(48);
        for (int i = 0; i < expectedFactors.length; i ++) {
            assert tempFactors.contains(expectedFactors[i]) : "The getFactor function did not provide an expected factor of " + expectedFactors[i];
        }

        // test the getSampleProductFactors
        int[] expectedSampleFactors = {6, 8, 12};
        ArrayList<Integer> tempSampleFactors = getSampleProductFactors(48, 6, 12);
        for (int i = 0; i < expectedSampleFactors.length; i ++) {
            assert tempSampleFactors.contains(expectedSampleFactors[i]) : "The getSampleProductFactors function did not provide an expected factor of " + expectedFactors[i] + " in the range provided";
        }
        assert !tempSampleFactors.contains(48) : "The sample factors should not contain the value of the highest factor";

        System.out.println("SampleProductFactors test cases pass");
    }
}