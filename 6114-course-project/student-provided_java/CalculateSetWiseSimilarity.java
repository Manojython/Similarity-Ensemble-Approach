import java.util.Random;
import java.util.ArrayList;
import Utils.TimeStepPlot;

public class CalculateSetWiseSimilarity
{
    // public static void main(String args[])
    // {
    //     // factors of 54
    //     ArrayList<Integer> factors54 = new ArrayList<Integer>();
    //     factors54.add(2); factors54.add(3); factors54.add(6); 
    //     factors54.add(9); factors54.add(18); factors54.add(27);

    //     // factors of 18
    //     ArrayList<Integer> factors18 = new ArrayList<Integer>();
    //     factors18.add(2); factors18.add(3); factors18.add(6); 
    //     factors18.add(9);

    //     // factors of 27
    //     ArrayList<Integer> factors27 = new ArrayList<Integer>();
    //     factors27.add(3); factors27.add(9);

    //     ArrayList<ArrayList<SampleFactorPair>> pairList = new ArrayList<ArrayList<SampleFactorPair>>();
    //     pairList.add(SampleFactorPair.FactorPairs(factors54, 54));
    //     pairList.add(SampleFactorPair.FactorPairs(factors18, 18));
    //     pairList.add(SampleFactorPair.FactorPairs(factors27, 27));

    //     TimeStepPlot meanVSProductSetSize = new TimeStepPlot(0.01, new String[] {"X", "Y"});
    //     meanVSProductSetSize.addXY(1.0, 1.0);
    //     TimeStepPlot standardDeviationVSProductSetSize = new TimeStepPlot(0.01, new String[] {"Size", "Mean"});
    //     standardDeviationVSProductSetSize.addXY(1.0, 1.0);

    //     (new CalculateSetWiseSimilarity(0.01, pairList, 
    //         new ChemicalSimilaritySet(28), meanVSProductSetSize,
    //         standardDeviationVSProductSetSize)).Test();
    // }

    public static final double APPROACH_ZERO_THRESHOLD = 0.5;

    // Comprises Ca and Cb
    private ArrayList<ArrayList<SampleFactorPair>> pairList;
    private double timeStep;
    private ChemicalSimilaritySet set;
    private TimeStepPlot meanVSProductSetSize;
    private TimeStepPlot standardDeviationVSProductSetSize;
    private ArrayList<SampleProductFactors> ca;

    public CalculateSetWiseSimilarity(double timeStep,
        ArrayList<ArrayList<SampleFactorPair>> pairList,
        ChemicalSimilaritySet set, TimeStepPlot meanVSProductSetSize,
        TimeStepPlot standardDeviationVSProductSetSize)
    {
        this.timeStep = timeStep;
        this.pairList = pairList;
        this.set = set;
        this.meanVSProductSetSize = meanVSProductSetSize;
        this.standardDeviationVSProductSetSize = standardDeviationVSProductSetSize;

        run();
    }

    private void run() {

        // #2 For each set a and b from collections Ca and Cb, respectively,
        // calculate r_ab(t_i) as previously described using only the optimal
        // threshold t_i from the background model. Be sure to use the actual
        // molecule structures annotated for each set.
        ArrayList<RawScore> pairRawScore = new ArrayList<RawScore>();
        for (int j = 0; j < pairList.size(); j ++) {
            RawScore score = new RawScore(timeStep, pairList.get(j), set);
            score.setSampleProduct(pairList.get(j).get(0).getSampleProduct());
            if (score.getASize() * score.getBSize() > 0) {
                pairRawScore.add(score);   
            }
        }

        // #3 Transform each r_ab(t_i) to z-score z_ab as described in step 10
        ArrayList<ZScore> zScores = new ArrayList<ZScore>();
        for (int j = 0; j < pairRawScore.size(); j ++) {
            zScores.add(new ZScore(pairRawScore.get(j),
                meanVSProductSetSize.getYValue(
                    pairRawScore.get(j).getASize() * pairRawScore.get(j).getBSize()),
                standardDeviationVSProductSetSize.getYValue(
                    pairRawScore.get(j).getASize() * pairRawScore.get(j).getBSize())));
        }

        // #4,5 Generate the escores
        ArrayList<EScore> eScores = new ArrayList<EScore>();
        for (int j = 0; j < zScores.size(); j ++) {
            eScores.add(new EScore(zScores.get(j), 
                pairList.size() * pairList.size()));
        }

        // #6 For each set a, rank all sets b from Cb by their EScore
        // where values approaching zero are the best scores.
        ArrayList<Integer> optimalSamples = new ArrayList<Integer>();
        for (int j = 0; j < eScores.size(); j ++) {
            if (eScores.get(j).getPScore() < APPROACH_ZERO_THRESHOLD) {
                if (!optimalSamples.contains(eScores.get(j).getZScore().getRawScore().getSampleProduct())) {
                    optimalSamples.add(eScores.get(j).getZScore().getRawScore().getSampleProduct());   
                }
            }
        }

        ca = new ArrayList<SampleProductFactors>();
        for (int i = 0; i < optimalSamples.size(); i ++) {
            ca.add(new SampleProductFactors(optimalSamples.get(i), Sample.SMIN, Sample.SMAX));
        }

        System.out.println("CalculateSetWiseSimilarity steps run without errors");
    }

    public ArrayList<SampleProductFactors> getOptimalCA() {
        return ca;
    }

    public void Test()
    {
        System.out.println("CalculateSetWiseSimilarity test cases pass");
    }
}
