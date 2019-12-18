import java.util.Random;
import java.util.ArrayList;

public class SampleFactorPair
{
    public static void main(String args[])
    {
        SampleFactorPair.Test();
    }

    public static final int PAIR_COUNT = 30;
    public static final Random RANDOM = new Random();

    public static ArrayList<SampleFactorPair> FactorPairs(ArrayList<Integer> factors, int sampleProduct)
    {
        ArrayList<SampleFactorPair> pairs = new ArrayList<SampleFactorPair>();
        ArrayList<Integer> localFactors = new ArrayList<Integer>(factors);

        int max = localFactors.size();
        if (max > PAIR_COUNT) {
            max = PAIR_COUNT;
        }

        for (int j = 0; j < max; j ++) {
            int factor = getRandomValue(localFactors);
            localFactors.remove(localFactors.indexOf(factor));
            pairs.add(new SampleFactorPair(factor, sampleProduct));
        }

        return pairs;
    }

    private static int getRandomValue(ArrayList<Integer> numbers) 
    {
        return numbers.get(RANDOM.nextInt(numbers.size()));
    }

    public static void Test() 
    {
        SampleFactorPair pair = new SampleFactorPair(6, 36);
        assert pair.getA() * pair.getB() == pair.getSampleProduct() : "A and B are not comparable factors that result in the sampleProduct";

        ArrayList<Integer> factors = new ArrayList<Integer>();
        factors.add(2); factors.add(3); factors.add(6); factors.add(9); 
        factors.add(18); factors.add(27); // factors of 54
        ArrayList<SampleFactorPair> pairs = FactorPairs(factors, 54);
        assert pairs.size() == 6 : "Pair a,b size is not restricted by the maximum size or PAIR_COUNT";
        for (int i = 0; i < pairs.size(); i ++) {
            assert pairs.get(i).getA() * pairs.get(i).getB() == pairs.get(i).getSampleProduct() : "A and B are not comparable factors that result in the sampleProduct";
        }

        System.out.println("SampleFactorPair test cases pass");
    }

    private int sampleProduct;
    private int factorA;
    private int factorB;

    public SampleFactorPair(int randomFactor, int sampleProduct)
    {
        this.factorA = randomFactor;
        this.factorB = sampleProduct / randomFactor;
        this.sampleProduct = sampleProduct;
    }

    public int getA()
    {
        return factorA;
    }

    public int getB()
    {
        return factorB;
    }

    public int getSampleProduct()
    {
        return sampleProduct;
    }
}