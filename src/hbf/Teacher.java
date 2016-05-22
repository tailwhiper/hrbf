package hbf;

import javax.xml.bind.UnmarshallerHandler;
import java.util.Arrays;
import java.util.List;
/**
 * Created by nikita on 21.05.2016.
 */
public class Teacher {
    private Network hbf;
    private double[] timeSeries;

    public double calcStandardDeviation(List<TrainingSet> series) {
        double sd = 0;
        for (TrainingSet set : series) {
            sd += Math.pow(set.getOutput() - hbf.getOutput(set.getInputs()), 2);
        }
        return sd / (series.size() - 1);
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
                         double standardDeviation,
                         int maxIterations,
                         double koef) {
        double sd = 1; // standard deviation;
        int iterations = 0;
        while (sd > standardDeviation || iterations++ < maxIterations) {

            for (TrainingSet set : series) {
                double difference = hbf.getOutput(set.getInputs()) - set.getOutput();//y-d

                hbf.w0 -= koef * difference;

                for (int i = 0; i < hbf.neurons.length; i++) {

                    double[] oldCenters = hbf.neurons[i].centers.clone();
                    double[][] oldQ = hbf.neurons[i].qMatrix.clone();
                    double expPart = Math.exp(-0.5 * hbf.neurons[i].u);
                    for (int j = 0; j < set.getInputs().length; j++) {
                        int qz = 0;//Qjr*zj
                        for (int r = 0; r < set.getInputs().length; r++) {
                            qz += oldQ[j][r] * hbf.neurons[i].z[j];
                            // Qmatrix
                            hbf.neurons[i].qMatrix[j][r] += koef
                                    * Math.exp(-0.5 * hbf.neurons[i].u)
                                    * difference
                                    * (set.getInputs()[j] - oldCenters[j])
                                    * hbf.neurons[i].z[r];
                        }
                        //centers
                        hbf.neurons[i].centers[j] += koef
                                * Math.exp(-0.5 * hbf.neurons[i].u)
                                * hbf.weights[i]
                                * difference
                                * qz;

                        // todo:  teach in order : Q -> c -> w
                    }
                    // weights
                    hbf.weights[i] -= koef * Math.exp(-0.5 * hbf.neurons[i].u) * difference;
                }
            }
            sd = calcStandardDeviation(series);
        }

    }
}
