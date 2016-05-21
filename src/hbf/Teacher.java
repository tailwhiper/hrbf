package hbf;

import java.util.List;

/**
 * Created by nikita on 21.05.2016.
 */
public class Teacher {
    private  Network hbf;
    private double [] timeSeries;

    public void teach(List<TrainingSet> series,
                      double standardDeviation,
                      int maxIterations){
        double sd = 1; // standard deviation;
        int iterations  = 0;
        while ( sd > standardDeviation || iterations++ < maxIterations){

            for (TrainingSet set : series){
                double y = hbf.getOutput(set.getInputs());
                double d = set.getOutput();

                hbf.w0 -= y - set.getOutput();

                for (int i = 0; i < hbf.neurons.length; i++){
                    hbf.weights[i] -= Math.exp(-0.5*hbf.neurons[i].u)*(y - d);

                    for (int j = 0; j < set.getInputs().length; j++){
                       // hbf.neurons[i].centers[j] -= Math.exp(-0.5*hbf.neurons[i].u)*hbf.weights[i]*
                        // todo:  teach in order : Q -> c -> w
                    }
                }
            }
        }

    }
}
