package neuralnetworks.hrbf;

import hbf.Network;
import hbf.Teacher;
import hbf.TrainingSet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<TrainingSet> convertToTrainingSet(double[] array, int inputCount) {
        List<TrainingSet> list = new ArrayList<TrainingSet>(array.length);
        for (int i = 0; i < array.length - inputCount; i++) {
            double[] inputs = new double[inputCount];
            for (int j = i; j < i + inputCount; j++) {
                inputs[j - i] = array[j];
            }
            list.add(new TrainingSet(inputs, array[i + inputCount]));
        }
        return list;
    }

    public static double[] readArray(String filename, int arrSize) throws IOException {
        double[] arr = new double[arrSize];
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String thisLine;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Double.parseDouble((reader.readLine()).split(" ")[1]);
        }
        reader.close();
        return arr;
    }

    public static double[] normalise(double min, double max) {
        return null;
    }

    public static double[] generateSin(int size) {

        double[] sinusSeries = new double[size];
        for (int i = 0; i < sinusSeries.length; i++) {
            sinusSeries[i] = Math.sin(0.005 * i);
        }
        return sinusSeries;
    }

    public static final int NEURONS_COUNT = 15;
    public static final int INPUT_COUNT = 3;

    public static void main(String[] args) {

        // double[] sinus = generateSin(50);
        double[] train = new double[500];
        double[] test = new double[100];

        try {
            train = readArray("C:\\hbf data\\MG_500.txt", 500);
            test = readArray("C:\\hbf data\\MG_500_test.txt", 50);

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<TrainingSet> list = convertToTrainingSet(train, INPUT_COUNT);
        List<TrainingSet> testlist = convertToTrainingSet(test, INPUT_COUNT);
        Network hbf = new Network(NEURONS_COUNT, INPUT_COUNT, -1, 1);
        Teacher teacher = new Teacher(hbf);
        teacher.teachBPG(list, 0.02, 50000, 0.0001, 0.001, 0.001);
        System.out.println("Test mse = " + teacher.calcMSE(testlist) + "; test err =" + teacher.calcError(testlist));
    }
}
