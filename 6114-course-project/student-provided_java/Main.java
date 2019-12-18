import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        CalculateReferenceParameters crp = new CalculateReferenceParameters();
        CalculateSetWiseSimilarity csws = new CalculateSetWiseSimilarity(crp.getOptimalTimeStep(), 
            crp.getFactorPairsLists(),
            crp.getChemicalSimilaritySet(),
            crp.getOptimalYMicro(),
            crp.getOptimalYSigma());
        ArrayList<SampleProductFactors> optimalCA = csws.getOptimalCA();
        if (optimalCA.size() > 0) {
            BuildSimilarityNetwork bsn = new BuildSimilarityNetwork(
                csws.getOptimalCA(),
                crp.getOptimalTimeStep(),
                crp.getChemicalSimilaritySet());   
        } else {
            System.out.println("Cannot run the BuildSimilarityNetwork as the CA is empty.");
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Total runtime is: " + ((double)(stopTime - startTime)) / 1000.0);
    }
}