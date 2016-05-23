package hbf;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
/**
 * Created by nikita on 21.05.2016.
 */
public class Teacher {
    private Network hbf;

    public Teacher(Network hbf) {
        this.hbf = hbf;
    }

    public double calcMSE(List<TrainingSet> series) {
        double sd = 0;
        for (TrainingSet set : series) {
            sd += Math.pow(set.getOutput() - hbf.getOutput(set.getInputs()), 2);
        }
        return sd / (series.size() - 1);
    }

    public double calcError(List<TrainingSet> series) {
        double err = 0;
        for (TrainingSet set : series) {
            err += Math.pow(set.getOutput() - hbf.getOutput(set.getInputs()), 2);
        }
        return err / 2;
    }
    /*k means teach centers*/
    public void teachKMeans(List<TrainingSet> series,
                            double standardDeviation,
                            int maxIterations,
                            double koef) {
        for (TrainingSet set : series) {
            double[] neuronsCentersDiffs = new double[hbf.neurons.length];
            for (int i = 0; i < hbf.neurons.length; i++) {
                double diffLength = 0;
                for (int j = 0; j < set.getInputs().length; j++) {
                    diffLength += Math.pow(hbf.neurons[i].centers[j] - set.getInputs()[j], 2);
                }
                neuronsCentersDiffs[i] = Math.sqrt(diffLength);
            }
            int winnerIndex = 0;
            double winnerValue = neuronsCentersDiffs[0];
            for (int i = 1; i < neuronsCentersDiffs.length; i++) {
                if (neuronsCentersDiffs[i] < winnerValue) {
                    winnerIndex = i;
                    winnerValue = neuronsCentersDiffs[i];
                }
            }


            for (int i = 0; i < set.getInputs().length; i++) {
                hbf.neurons[winnerIndex].centers[i] += koef * (set.getInputs()[i] - hbf.neurons[winnerIndex].centers[i]);
                //todo: уменьшать koef со временем
            }
        }


    }

    /* Back propogation*/
    public void teachBPG(List<TrainingSet> series,
                         double maxMSE,
                         int maxIterations,
                         double koefCenters,
                         double koefWeights,
                         double koefQ) {
        double mse = 1; // standard deviation;
        double err = 0;
        int iterations = 0;
        while (mse > maxMSE && iterations < maxIterations) {

            for (TrainingSet set : series) {
                double difference = hbf.getOutput(set.getInputs()) - set.getOutput();//y-d

                hbf.w0 -= koefWeights * difference;

                for (int i = 0; i < hbf.neurons.length; i++) {

                    double[] oldCenters = Arrays.copyOf(hbf.neurons[i].centers, hbf.neurons[i].centers.length);
                    double[][] oldQ = new double[hbf.neurons[i].qMatrix.length][];
                    // copy Q
                    for (int k = 0; k < hbf.neurons[i].qMatrix.length; k++) {
                        oldQ[k] = Arrays.copyOf(hbf.neurons[i].qMatrix[k], hbf.neurons[i].qMatrix[k].length);
                    }
                    double expPart = Math.exp(-0.5 * hbf.neurons[i].u);

                    for (int j = 0; j < set.getInputs().length; j++) {

                        int qz = 0;//Qjr*zj

                        for (int r = 0; r < set.getInputs().length; r++) {

                            qz += oldQ[j][r] * hbf.neurons[i].z[j];
                            // Qmatrix
                            hbf.neurons[i].qMatrix[j][r] += koefQ
                                    * Math.exp(-0.5 * hbf.neurons[i].u)
                                    * difference
                                    * (set.getInputs()[j] - oldCenters[j])
                                    * hbf.neurons[i].z[r];

                        }
                        //centers
                        hbf.neurons[i].centers[j] += koefCenters
                                * Math.exp(-0.5 * hbf.neurons[i].u)
                                * hbf.weights[i]
                                * difference
                                * qz;

                    }
                    // weights
                    hbf.weights[i] -= koefWeights * Math.exp(-0.5 * hbf.neurons[i].u) * difference;
                }
            }
            mse = calcMSE(series);
            err = calcError(series);
            System.out.println("iter =" + iterations + "; mse = " + mse + "; err = " + err);
            iterations++;
        }

    }
}
