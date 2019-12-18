import java.util.ArrayList;

public class ZScore {

    public static void main(String args[]) {
        ArrayList<Integer> factors = new ArrayList<Integer>();
        factors.add(2); factors.add(3); factors.add(6); factors.add(9); 
        factors.add(18); factors.add(27); // factors of 54

        RawScore score = new RawScore(0.01, SampleFactorPair.FactorPairs(factors, 54), 
            new ChemicalSimilaritySet(28));

        (new ZScore(score, 10, 10)).Test();
    }

    private double zscore;
    private RawScore rawScore;

    public ZScore(RawScore rawScore, double y_micro, double y_sigma) {
        this.rawScore = rawScore;
        zscore = calculateZ(rawScore.getRawScore(), y_micro, y_sigma); 
    }

    // z_ab = (r_ab - y_m(|a|*|b|)) / y_s(|a|*|b|)
    private double calculateZ(double rawScore, double y_micro,
        double y_sigma) {
        return (rawScore - y_micro) / y_sigma;
    }

    public double getZScore() {
        return zscore;
    }

    public RawScore getRawScore() {
        return rawScore;
    }

    public void Test() {
        assert Math.abs(calculateZ(1, 0.5, 1) - 0.5) < 0.00001 : "Zscore not calculated correctly";
        assert Math.abs(calculateZ(1, 0.5, 0.5) - 1.0) < 0.00001 : "Zscore not calculated correctly";

        System.out.println("ZScore test cases pass");
    }
}