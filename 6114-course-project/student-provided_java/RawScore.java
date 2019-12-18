import java.util.ArrayList;

public class RawScore
{
    public static void main(String args[])
    {

        ArrayList<Integer> factors = new ArrayList<Integer>();
        factors.add(2); factors.add(3); factors.add(6); factors.add(9); 
        factors.add(18); factors.add(27); // factors of 54

        (new RawScore(0.01, SampleFactorPair.FactorPairs(factors, 54), 
            new ChemicalSimilaritySet(28))).Test();
    }

    private int sampleProduct;
    private double timeStep;
    private double rawScore;
    private int setASize;
    private int setBSize;
    private ArrayList<Integer> setA;
    private ArrayList<Integer> setB;

    public RawScore(double timeStep, ArrayList<SampleFactorPair> pairs, 
        ChemicalSimilaritySet set)
    {
        this.rawScore = calculateRawScore(timeStep, pairs, set);
        this.setA = determineSetA(timeStep, pairs, set);
        this.setB = determineSetB(timeStep, pairs, set);
        this.timeStep = timeStep;
    }

    public RawScore(double timeStep, ArrayList<Integer> setA, 
        ArrayList<Integer> setB, ChemicalSimilaritySet set)
    {
        this.rawScore = calculateRawScore(timeStep, setA, setB, set);
        this.setA = setA;
        this.setB = setB;
        this.timeStep = timeStep;
    }

    public int getASize()
    {
        return setA.size();
    }

    public int getBSize()
    {
        return setB.size();
    }

    public ArrayList<Integer> getSetA()
    {
        return setA;
    }

    public ArrayList<Integer> getSetB()
    {
        return setB;
    }

    public double getRawScore()
    {
        return rawScore;
    }

    public double getTimeStep()
    {
        return timeStep;
    }

    public int getSampleProduct()
    {
        return sampleProduct;
    }

    public void setSampleProduct(int sample)
    {
        sampleProduct = sample;
    }

    private double calculateRawScore(double timeStep, ArrayList<Integer> setA, 
        ArrayList<Integer> setB, ChemicalSimilaritySet set)
    {
        double score = 0.0;

        for (int i = 0; i < setA.size(); i ++) {
            for (int j = 0; j < setB.size(); j ++) {
                if (set.getChemicalSimilarity(setA.get(i), setB.get(j))  
                    > timeStep) {
                    score += set.getChemicalSimilarity(setA.get(i), setB.get(j));
                }
            }
        }

        return score;
    }

    private double calculateRawScore(double timeStep, 
        ArrayList<SampleFactorPair> pairs, ChemicalSimilaritySet set)
    {
        double score = 0.0;

        for (int i = 0; i < pairs.size(); i ++) {
            if (set.getChemicalSimilarity(pairs.get(i).getA(), 
                pairs.get(i).getB())  > timeStep) 
            {
                score += set.getChemicalSimilarity(pairs.get(i).getA(), 
                    pairs.get(i).getB());
            }
        }

        return score;
    }

    /*
     * Calculate the size of the set A where A is composed of all values of a where
     * c_ab is greater than the time step.
     */
    private ArrayList<Integer> determineSetA(double timeStep, 
        ArrayList<SampleFactorPair> pairs, ChemicalSimilaritySet set)
    {
        ArrayList<Integer> aSet = new ArrayList<Integer>();

        for (int i = 0; i < pairs.size(); i ++) {
            if (set.getChemicalSimilarity(pairs.get(i).getA(), 
                pairs.get(i).getB())  > timeStep) 
            {
                if (!aSet.contains(pairs.get(i).getA())) {
                    aSet.add(pairs.get(i).getA());
                }
            }
        }

        return aSet;
    }

    /*
     * Calculate the size of the set B where B is composed of all values of b where
     * c_ab is greater than the time step.
     */
    private ArrayList<Integer> determineSetB(double timeStep, 
        ArrayList<SampleFactorPair> pairs, ChemicalSimilaritySet set)
    {
        ArrayList<Integer> bSet = new ArrayList<Integer>();

        for (int i = 0; i < pairs.size(); i ++) {
            if (set.getChemicalSimilarity(pairs.get(i).getA(), 
                pairs.get(i).getB())  > timeStep) 
            {
                if (!bSet.contains(pairs.get(i).getB())) {
                    bSet.add(pairs.get(i).getB());
                }
            }
        }

        return bSet;
    }

    public void Test() 
    {
        ArrayList<Integer> factorsA = new ArrayList<Integer>();
        factorsA.add(2); factorsA.add(3); factorsA.add(6); factorsA.add(9); 
        factorsA.add(18); factorsA.add(27); // factors of 54

        ArrayList<Integer> factorsB = new ArrayList<Integer>();
        factorsB.add(2); factorsB.add(3); factorsB.add(6); factorsB.add(9); 
        factorsB.add(18); factorsB.add(27); // factors of 54

        RawScore tempFactorPair = new RawScore(0.01, SampleFactorPair.FactorPairs(factorsA, 54), 
            new ChemicalSimilaritySet(28));
        assert tempFactorPair.getRawScore() > 0.01 : "Raw score should be greater than the time step";
        assert tempFactorPair.getASize() >= 1 : "Size of a should be ≥ 1 if the above is correct";
        assert tempFactorPair.getBSize() >= 1 : "Size of b should be ≥ 1 if the above is correct";

        RawScore tempSets = new RawScore(0.01, factorsA, factorsB, 
            new ChemicalSimilaritySet(28));
        assert tempSets.getRawScore() > 0.01 : "Raw score should be greater than the time step";
        assert tempSets.getASize() >= 1 : "Size of a should be ≥ 1 if the above is correct";
        assert tempSets.getBSize() >= 1 : "Size of b should be ≥ 1 if the above is correct";

        System.out.println("RawScore test cases pass");
    }
}