package hbf;

/**
 * Created by nikita on 21.05.2016.
 */
public class TrainingSet {
    private double [] inputs;
    private double output;

    public TrainingSet(double[] inputs, double output) {
        this.inputs = inputs;
        this.output = output;
    }

    public double getOutput() {

        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double[] getInputs() {
        return inputs;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }
}
