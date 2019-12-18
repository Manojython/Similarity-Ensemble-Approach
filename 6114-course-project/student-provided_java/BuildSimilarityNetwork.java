import java.util.Random;
import java.util.ArrayList;
import Utils.TimeStepPlot;
import Utils.Primes;
import Utils.Stats;
import Kruskals.Graph;

public class BuildSimilarityNetwork
{
    public static void main(String args[])
    {
        int[] sampleProducts = Sample.generate(Sample.SAMPLE_SIZE, Sample.SMIN * Sample.SMIN, Sample.SMAX * Sample.SMAX);

        // #3 For each product of set's sizes s_i, calculate all of its
        // integer factors such smin ≤ f_i ≤ smax
        ArrayList<SampleProductFactors> sampleProductFactors = new ArrayList<SampleProductFactors>();
        for (int i = 0; i < sampleProducts.length; i ++) {
            sampleProductFactors.add(new SampleProductFactors(sampleProducts[i], Sample.SMIN, Sample.SMAX));
        }

        // SampleProductFactors
        (new BuildSimilarityNetwork(sampleProductFactors,0.01,new ChemicalSimilaritySet(Sample.SAMPLE_SIZE))).Test();
    }

    private ArrayList<SampleProductFactors> ca;
    private double step;
    private ChemicalSimilaritySet set;

    public BuildSimilarityNetwork(ArrayList<SampleProductFactors> ca, 
        double step, ChemicalSimilaritySet set) {
        this.ca = ca;
        this.step  = step;
        this.set = set;
        run();
    }

    private void run() {
        // #1 Calculate the similarity ensemble E-values between all sets
        // a_i and a_j from Ca versus itself. Ca is simply our Integer
        // ArrayList.

        // We setup a two dimensional RawScore array that will allow
        // to contain all of our a_i to a_j and create our respective TimeStepPlot
        RawScore[][] rawScores = new RawScore[ca.size()][ca.size()];
        TimeStepPlot sizeVSRawScore = new TimeStepPlot(step, new String[] {"X", "Y"});
        for(int i=0;i<ca.size();i++) {
            for(int j=0;j<ca.size();j++) {
                rawScores[i][j] = new RawScore(step, ca.get(i).getFactors(), ca.get(j).getFactors(), set);
                sizeVSRawScore.addXY(
                    rawScores[i][j].getASize() * rawScores[i][j].getBSize(),
                    rawScores[i][j].getRawScore());
            }
        }

        // Determine the sizeVSMean plot from the sizeVSRawScore plot
        TimeStepPlot sizeVSMean = new TimeStepPlot(step, new String[] {"Size", "Mean"});
        int maxProductSize = (int) sizeVSRawScore.getMaxX();
        for (int productSize = 0; productSize <= maxProductSize; productSize ++) {
            double mean = Stats.Mean(sizeVSRawScore.getYArray(productSize));
            if (mean > 0) {
                sizeVSMean.addXY(productSize, mean);
            }
        }

        // Determine the sizeVSStandardDev plot from the sizeVSMean plot
        TimeStepPlot sizeVSStandardDev = new TimeStepPlot(step, new String[] {"Size", "Std"});
        for (int productSize = 0; productSize <= maxProductSize; productSize ++) {

            int binValCount = 0;
            int count = sizeVSRawScore.getYArray(productSize).length;
            
            //based on number of values against each productSize, we generate prime numbers which are later used to dynamically determine the
            // number of data points in each bin (should be greater than 5).
            ArrayList<Integer> prime = Primes.generate(count);
            
            if(count < 5) {
                double std = Stats.StandardDeviation(sizeVSRawScore.getYArray(productSize), 
                    Stats.Mean(sizeVSRawScore.getYArray(productSize)));
                
                if (std > 0) { 
                    sizeVSStandardDev.addXY(productSize, std);
                }
                
            } else {
                for(int p = 0; p < prime.size(); p++) {
                    if((count % prime.get(p)) == 0) {
                        int factor = count / prime.get(p);
                        if(factor > prime.get(p)) {
                            binValCount = factor;
                        } else {
                            binValCount = prime.get(p);
                        }   
                    }
                }
                
                double productArray[] = sizeVSRawScore.getYArray(productSize);
                
                // Binning all the data points in the productArray with respect to previously determined binValCount, calculate mean, std & plot.
                for(int l = 0; l < productArray.length; l++) {
                    if(binValCount < 5) {
                        binValCount = productArray.length;
                    }
                    double temp[] = new double[binValCount];
                    for(int m = l; m < binValCount-1; m++) {
                        temp[m] = productArray[m];
                    }
                    
                    double s = Stats.StandardDeviation(temp, Stats.Mean(temp));
                    
                    if (s > 0) { 
                        sizeVSStandardDev.addXY(productSize, s);
                    }
                     
                    l = l + binValCount;
                }
            }
        }

        // We setup a two dimensional EScore array that will allow
        // to contain all of our 
        EScore[][] eScores = new EScore[ca.size()][ca.size()];
        
        for(int i=0;i<ca.size();i++) {
            for(int j=0;j<ca.size();j++) {
                eScores[i][j] = new EScore(
                    new ZScore(rawScores[i][j], 
                        sizeVSMean.getYValue(
                            rawScores[i][j].getASize() * rawScores[i][j].getBSize()),
                        sizeVSStandardDev.getYValue(
                            rawScores[i][j].getASize() * rawScores[i][j].getBSize())), 
                    ca.size() * ca.size());
            }
        }

        // #3 Kruskals ...
        Graph graph = new Graph(ca.size(), (ca.size() * (ca.size() + 1)) / 2);
        int counter = 0;
        for(int i=0;i<ca.size();i++) {
            for(int j=i;j<ca.size();j++) {
                graph.edge[counter].src = i;
                graph.edge[counter].dest = j;
                graph.edge[counter].srcSample = ca.get(i).getSampleProduct();
                graph.edge[counter].destSample = ca.get(j).getSampleProduct();
                graph.edge[counter].weight = eScores[i][j].getEScore();
                counter++;
            }
        }

        graph.kruskal();
        // graph.print();
        graph.save();

        System.out.println("BuildSimilarityNetwork steps run without errors");

    }

    public void Test() {
        System.out.println("BuildSimilarityNetwork test cases pass");
    }
}