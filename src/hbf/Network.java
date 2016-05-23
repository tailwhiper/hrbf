package hbf;

import java.util.Random;

/**
 * Created by nikita on 21.05.2016.
 */
public class Network {
     double w0;
     Neuron [] neurons;
     double [] weights;
    public double getOutput (double [] inputs){
        double y = w0;
        for (int i = 0; i < neurons.length; i++){
            y += weights[0]*Math.exp(-0.5*neurons[i].getOutput(inputs));
        }
        return y;
    }

    public Network(int neuronCount, int inputCount, double min, double max) {
        neurons = new Neuron[neuronCount];
        weights = new double[neuronCount];
        Random r = new Random();
        for (int i = 0; i < neuronCount; i++) {
            neurons[i] = new Neuron(inputCount, min, max);
            weights[i] = r.nextDouble();
        }
    }
}
