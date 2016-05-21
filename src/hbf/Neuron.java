package hbf;

import java.util.List;

/**
 * Created by nikita on 21.05.2016.
 */
public class Neuron {
    double[][] qMatrix;//квадратная
    double[] centers;
    double[] z;// snapshot z
    double u; // snapshot u

    public double getOutput(double[] inputs) {

        for (int i = 0; i < centers.length; i++) {

            inputs[i] -= centers[i];
        }

        for (int i = 0; i < z.length; i++) {
            z[i] = 0;
            for (int j = 0; j < z.length; j++) {
                z[i] += inputs[j] * qMatrix[i][j]; //не уверен насчет индексов qMatrix, поменять местами
            }
        }
        u = 0;
        for (int i = 0; i < z.length; i++) {
            u += Math.pow(z[i], 2);
        }
        return u;
    }

    public Neuron(int inputSize) {
        qMatrix = new double[inputSize][inputSize];
        centers = new double[inputSize];
        z = new double[centers.length];
        u = 0;
    }
}
