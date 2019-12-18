import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import Utils.TableXY;
import Utils.Stats;
import Utils.TimeStepPlot;
import Utils.Fitter;
import Utils.Gaussian;
import Utils.Gumbel;
import Utils.Histogram;

public class CalculateReferenceParameters
{
    // public static void main(String args[])
    // {
    //     (new CalculateReferenceParameters()).Test();
    // }

    public static final double BEST_FIT_STEP_SIZE = 0.1;

    private Random random;
    private ArrayList<ArrayList<SampleFactorPair>> factorPairsLists;
    private ChemicalSimilaritySet set;
    private TimeStepPlot optimalYMicro;
    private TimeStepPlot optimalYSigma;
    private double optimalTimeStep;

    public CalculateReferenceParameters()
    {
        random = new Random();
        run();
    }

    private void run()
    {
        // #1 choose minimum and maximum values, such that they will be 
        // representative of molecule sets annotated in the database
        int smin = Sample.SMIN;
        int smax = Sample.SMAX;

        // #2 sample at least 1000 integers from the range smin^2 to smax^2
        int[] sampleProducts = Sample.generate(Sample.SAMPLE_SIZE, smin * smin, smax * smax);

        // #3 For each product of set's sizes s_i, calculate all of its
        // integer factors such smin ≤ f_i ≤ smax
        ArrayList<SampleProductFactors> sampleProductFactors = new ArrayList<SampleProductFactors>();
        for (int i = 0; i < sampleProducts.length; i ++) {
            sampleProductFactors.add(new SampleProductFactors(sampleProducts[i], smin, smax));
        }

        // #4 For each s_i choose 30 of its f_i at random and construct two 
        // sets, a and b, consisting of f_i and sr_i/f_i respectively.
        factorPairsLists = new ArrayList<ArrayList<SampleFactorPair>>();
        for (int i = 0; i < sampleProductFactors.size(); i ++) {
            // ignore samples where their factors are empty
            if (sampleProductFactors.get(i).getFactors().size() == 0) continue;

            factorPairsLists.add(SampleFactorPair.FactorPairs(
                sampleProductFactors.get(i).getFactors(), 
                sampleProductFactors.get(i).getSampleProduct()));
        }

        // #5 use pipeline pilot to calculate the chemical similarity of the
        // values.
        set = new ChemicalSimilaritySet(Sample.SAMPLE_SIZE);

        // #6 For t_i, where 0 ≤ t_i ≤ 1 with step size 0.01, calculate a "raw 
        // score" r_ab(t_i) equal to the sum of all c_ab where c_ab > t_i. Store
        // all calculated r_ab(t_i), along with the sizes of sets a and b.
        double[] timeSteps = new double[100];
        for (int i = 0; i < timeSteps.length; i ++) {
            timeSteps[i] = ((double) i) / 100.0;
        }

        ArrayList<ArrayList<RawScore>> rawScores = new ArrayList<ArrayList<RawScore>>();
        for (int i = 0; i < timeSteps.length; i ++) {
            ArrayList<RawScore> pairRawScore = new ArrayList<RawScore>();
            for (int j = 0; j < factorPairsLists.size(); j ++) {
                RawScore score = new RawScore(timeSteps[i], factorPairsLists.get(j), set);
                if (score.getASize() * score.getBSize() > 0) {
                    pairRawScore.add(score);   
                }
            }
            rawScores.add(pairRawScore);
        }

        // #7 plot each of the rawscore a and b size pairs for each time step
        // Save each plot table as a CSV, Manually render the plot in Excel
        ArrayList<TimeStepPlot> rawScoreVSProductSetSizePlots = new ArrayList<TimeStepPlot>();
        for (int i = 0; i < timeSteps.length; i ++) {
            TimeStepPlot plot = new TimeStepPlot(timeSteps[i], new String[] {"Product Size", "Raw Score"});
            ArrayList<RawScore> rawScoreList = rawScores.get(i);

            for (int j = 0; j < rawScoreList.size(); j ++) {
                plot.addXY(
                    rawScoreList.get(j).getASize() * rawScoreList.get(j).getBSize(),
                    rawScoreList.get(j).getRawScore());
            }

            plot.saveCSV("RawScore/rawscore_");
            rawScoreVSProductSetSizePlots.add(plot);
        }

        // #8 calculate the mean and std for all plots determined above
        // Save each plot table as a CSV, Manually render the plot in Excel
        ArrayList<TimeStepPlot> meanVSProductSetSizePlots = new ArrayList<TimeStepPlot>();
        for (int i = 0; i < timeSteps.length; i ++) {
            TimeStepPlot plot = new TimeStepPlot(timeSteps[i], new String[] {"Product Size", "Mean"});
            int maxProductSize = (int) rawScoreVSProductSetSizePlots.get(i).getMaxX();
            
            for (int productSize = 0; productSize <= maxProductSize; productSize ++) {

                double mean = Stats.Mean(rawScoreVSProductSetSizePlots.get(i).getYArray(productSize));

                if (mean > 0) { 
                    plot.addXY(productSize, mean);
                }
            }

            plot.saveCSV("SizeVSMean/sizevsmean_");
            meanVSProductSetSizePlots.add(plot);  
        }
        
        
        // #9 calculate the mean and std by binning x-axis values for all plots determined above
        // Save each plot table of binned values vs std as a CSV, Manually render the plot in Excel
        ArrayList<TimeStepPlot> stdVSProductSetSizePlots = new ArrayList<TimeStepPlot>();
        for (int i = 0; i < timeSteps.length; i ++) {
        	
            TimeStepPlot plot = new TimeStepPlot(timeSteps[i], new String[] {"Product Size", "Std"});
            int maxProductSize = (int) rawScoreVSProductSetSizePlots.get(i).getMaxX();

            for (int productSize = 0; productSize <= maxProductSize; productSize ++) {

                double mean = Stats.Mean(rawScoreVSProductSetSizePlots.get(i).getYArray(productSize));
                double std = Stats.StandardDeviation(rawScoreVSProductSetSizePlots.get(i).getYArray(productSize), mean);
                
                if (std > 0) { 
                    plot.addXY(productSize, std);
                }
            }
            
            plot.saveCSV("SizeVSStd/sizevsstd_");
            stdVSProductSetSizePlots.add(plot);
        }

        // #10 For each plot, use the fitted y_m and y_s to transofmr all 
        // original points (|a| x |b|, r_ab) to their z-scores z_ab = 
        // (r_ab - y_m(|a| x |b|)) / y_s(|a| x |b|). Construct a historgam of
        // these scores.
        ArrayList<TimeStepPlot> zScoreVSProductSetSizePlots = new ArrayList<TimeStepPlot>();
        ArrayList<ArrayList<ZScore>> zScores = new ArrayList<ArrayList<ZScore>>();
        for (int i = 0; i < timeSteps.length; i ++) {
            ArrayList<ZScore> zScoreList = new ArrayList<ZScore>();
            ArrayList<Double> list = new ArrayList<Double>();
            ArrayList<RawScore> rawScoreList = rawScores.get(i);

            for (int j = 0; j < rawScoreList.size(); j ++) {
                ZScore zscore = new ZScore(rawScoreList.get(j), 
                    meanVSProductSetSizePlots.get(i).getYValue(
                        rawScoreList.get(j).getASize() * rawScoreList.get(j).getBSize()),
                    stdVSProductSetSizePlots.get(i).getYValue(
                        rawScoreList.get(j).getASize() * rawScoreList.get(j).getBSize()));

                list.add(zscore.getZScore());
                zScoreList.add(zscore);
            }

            zScores.add(zScoreList);
            TimeStepPlot plot = (new Histogram(list)).generateTimeStepPlot(timeSteps[i]);
            plot.saveCSV("ZScore/zscore_");
            zScoreVSProductSetSizePlots.add(plot);
        }

        // #11 fit histogram of zScores to Gaussian and EVD distributions
        double[] fitScore = new double[timeSteps.length];
        for (int i = 0; i < zScoreVSProductSetSizePlots.size(); i ++) {
            // set default value for fitScore
            fitScore[i] = Double.MAX_VALUE;

        	// getting X and Y values from previously plotted histogram of zScores
            double[][] values = zScoreVSProductSetSizePlots.get(i).getXY();
            // Gaussian fitter throws an error otherwise
            if (values.length < 3) continue;

            double[] x = new double[values.length];
            double[] y = new double[values.length];
            for(int j = 0; j < values.length; j ++) {
                x[j] = values[j][0]; // scores
                y[j] = values[j][1]; // frequencies
            }
            
            // nonlinearly fitting data to Extreme Value Type I (Gumbel) distribution
            fitScore[i] = calculateFitScore(Gaussian.gaussianPdf(x, y, 
                Fitter.gaussian(x, y)), Gumbel.gumbelPdf(y), y);
        }
        
        
        // #12 Based on the goodness of fit, such as each fit's observed-vs.
        // -expected value, select the threshold choice timestep, such that the 
        // histogram best fits an EVD instead of a Gaussian distribution.
        int minIndex = 0;
        for (int i = 0; i < timeSteps.length; i ++) {
            if (fitScore[i] < fitScore[minIndex]) {
                minIndex = i;
            }
        }
        
        // #13 Record the chosen timeStamp and formula for y_micro and y_sigma.
        // These values comprise the random background model. All other plots,
        // histograms, and formulae may be discarded at this point.
        optimalTimeStep = timeSteps[minIndex];
        optimalYMicro = meanVSProductSetSizePlots.get(minIndex);
        optimalYSigma = stdVSProductSetSizePlots.get(minIndex);

        System.out.println("Best timestep is " + timeSteps[minIndex] + " at fit " + fitScore[minIndex]);
        System.out.print("y_micro is "); optimalYMicro.printExponentConstants();
        System.out.print("y_sigma is "); optimalYSigma.printExponentConstants();

        System.out.println("CalculateReferenceParameters steps run without errors");
    }

    // The smaller the score the better the fit.
    private double calculateFitScore(ArrayList<Double> gaussian_params, 
        ArrayList<Double> evdType1_params, double[] y) {
        
        // comparing the difference between zScores and evdDistVals and storring in ArrayList distance
        double[] distanceEVD = new double[y.length];
        double[] distanceGaussian = new double[y.length];
        for(int q = 0; q < y.length; q++) {
            // we want non negative numbers 
            distanceEVD[q] = Math.abs(y[q] - evdType1_params.get(q));
            distanceGaussian[q] = Math.abs(y[q] - gaussian_params.get(q));
        }

        // If the denominator is greater than the numerator then the gaussian
        // fit is better than the EVD fit for the current plot.
        return Stats.Mean(distanceEVD) / Stats.Mean(distanceGaussian);
    }

    public ArrayList<ArrayList<SampleFactorPair>> getFactorPairsLists() {
        return factorPairsLists;
    }

    public ChemicalSimilaritySet getChemicalSimilaritySet() {
        return set;
    }

    public double getOptimalTimeStep() {
        return optimalTimeStep;
    }

    public TimeStepPlot getOptimalYMicro() {
        return optimalYMicro;
    }

    public TimeStepPlot getOptimalYSigma() {
        return optimalYSigma;
    }

    public void Test() 
    {
        System.out.println("CalculateReferenceParameters test cases pass");
    }
}