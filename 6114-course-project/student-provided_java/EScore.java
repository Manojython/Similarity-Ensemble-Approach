import java.lang.Math;
import java.util.ArrayList;

public class EScore {

    public static void main(String args[]) {

        ArrayList<Integer> factors = new ArrayList<Integer>();
        factors.add(2); factors.add(3); factors.add(6); factors.add(9); 
        factors.add(18); factors.add(27); // factors of 54

        RawScore score = new RawScore(0.01, SampleFactorPair.FactorPairs(factors, 54), 
            new ChemicalSimilaritySet(28));

        (new EScore(new ZScore(score, 10, 10), 10)).Test();
    }

    // Euler Mascheroni constant
    public static final double EU = 0.577215665;

    private double escore;
    private double pscore;
    private ZScore zScore;

    public EScore(ZScore zScore, int productSize) {
        this.zScore = zScore;
        pscore = calculateP(zScore.getZScore());
        escore = calculateE(pscore, productSize);
    }

    private double calculateP(double zScore) {
        return 1 - Math.pow(Math.E, - Math.pow(Math.E,
            (zScore * Math.PI) / (Math.sqrt(6) - EU)));
    }

    private double calculateE(double p, int productSize) {
        return p * productSize;
    }

    public double getPScore() {
        return pscore;
    }

    public double getEScore() {
        return escore;
    }

    public ZScore getZScore() {
        return zScore;
    }

    public void Test() {
        assert Math.abs(calculateP(0.78463) - 0.9760220183362339) < 0.00001 : "EScore's p not calculated correctly";
        assert Math.abs(calculateE(0.9760, 10) - 9.760) < 0.00001 : "EScore not calculated correctly";

        System.out.println("EScore test cases pass");
    }
}