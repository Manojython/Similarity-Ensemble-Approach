import java.util.Random;
import java.util.ArrayList;

public class ChemicalSimilaritySet
{
    public static void main(String args[])
    {
        (new ChemicalSimilaritySet(0)).Test();
    }

    public static final double NOT_IN_SET = -1.0;
    private Random random;
    private double[][] set;

    public ChemicalSimilaritySet(int size)
    {
        random = new Random();
        set = generateChemicalSimilarity(size);
    }

    public double getChemicalSimilarity(int a, int b) 
    {
        if (a >= set.length || b >= set[a].length) 
            return NOT_IN_SET;

        return set[a][b];
    }

    private double[][] generateChemicalSimilarity(int size) 
    {
        double[][] temp = new double[size][size];
        for (int i = 0; i < temp.length; i ++) {
            for (int j = 0; j < temp[0].length; j ++) {
                if (i == j) {
                    temp[i][j] = 1;
                } else {
                    temp[i][j] = random.nextDouble();   
                }
            }
        }

        return temp;
    }

    public void Test() 
    {
        // test generateChemicalSimilarity
        double[][] tempSet = generateChemicalSimilarity(10);
        assert tempSet.length == 10 && tempSet[0].length == 10 : "Chemical Similarity creation too small";
        for (int i = 0; i < tempSet.length; i ++) {
            for (int j = 0; j < tempSet[0].length; j ++) {
                if (i == j) {
                    assert tempSet[i][j] == 1 : "Chemical Similarity is not symmetric";
                } else {
                    assert tempSet[i][j] > 0 && tempSet[i][j] < 1 : "Chemical Similarity at " + i + "," + j + " is not uniform between 0 and 1";    
                }
                
            }
        }

        this.set = tempSet;
        assert getChemicalSimilarity(10, 10) == NOT_IN_SET : "If the requested a and b are out of the set then a -1 should be provided";
        assert getChemicalSimilarity(10, 11) == NOT_IN_SET : "If the requested a and b are out of the set then a -1 should be provided";
        assert getChemicalSimilarity(11, 10) == NOT_IN_SET : "If the requested a and b are out of the set then a -1 should be provided";

        System.out.println("ChemicalSimilaritySet test cases pass");
    }
}