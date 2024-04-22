import com.iso53.algorithm.Incident;
import com.iso53.algorithm.ProblemData;
import com.iso53.algorithm.SimulatedAnnealing;
import com.iso53.algorithm.Solution;
import com.iso53.mutation.*;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        findBestMutation();
    }

    public static void findBestParameters() {
        final String MAIN_PATH = "C:\\Users\\termi\\Documents\\Projects\\SchoolProjects\\SA-For-Post-Disaster" +
                "-Management\\src\\main\\resources\\";

        ProblemData.init(
                new File(MAIN_PATH + "Process Time and Capabilities.json"),
                new File(MAIN_PATH + "Incident Types and Locations.json"),
                new File(MAIN_PATH + "Unit Types.json"),
                new File(MAIN_PATH + "Distance Matrix.json"));

        // Create a DecimalFormat object with dot as the decimal separator
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.0000", symbols);

        System.out.print("\t");
        for (double coolingRate = 0.05; coolingRate < 1; coolingRate += 0.05) {
            System.out.printf("\t" + df.format(coolingRate));
        }

        for (int temperature = 5000; temperature < 100000; temperature += 5000) {
            System.out.printf("\n%d\t", temperature);
            for (double coolingRate = 0.05; coolingRate < 1; coolingRate += 0.05) {
                SimulatedAnnealing SA = new SimulatedAnnealing(10000, 0.0003, ProblemData.INCIDENTS);
                Solution s = SA.run();
                System.out.print(df.format(s.getScore()) + ", ");
            }
        }
    }

    public static void findBestMutation() {
        final String MAIN_PATH = "C:\\Users\\termi\\Documents\\Projects\\SchoolProjects\\SA-For-Post-Disaster" +
                "-Management\\src\\main\\resources\\";

        ProblemData.init(
                new File(MAIN_PATH + "Process Time and Capabilities.json"),
                new File(MAIN_PATH + "Incident Types and Locations.json"),
                new File(MAIN_PATH + "Unit Types.json"),
                new File(MAIN_PATH + "Distance Matrix.json"));

        // Create a DecimalFormat object with dot as the decimal separator
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.0000", symbols);

        List<Incident[]> varietyOfIncident = new ArrayList<>(31);
        for (int i = 0; i < 31; i++) {
            Incident[] incidents = Arrays.copyOf(ProblemData.INCIDENTS, ProblemData.INCIDENTS.length);
            Collections.shuffle(Arrays.asList(incidents));
            varietyOfIncident.add(incidents);
        }

        List<MutationAlgorithm> algorithms = List.of(
                new InsertAlgorithm(),
                new InversionAlgorithm(),
                new ScrambleAlgorithm(),
                new SwapAlgorithm());

        for (MutationAlgorithm mutationAlgorithm : algorithms) {
            System.out.print(mutationAlgorithm.getClass().getSimpleName().replace("Algorithm", "") + ",\t");
        }

        for (Incident[] incidents : varietyOfIncident) {
            System.out.println();
            for (MutationAlgorithm algorithm : algorithms) {
                SimulatedAnnealing SA = new SimulatedAnnealing(100, 0.98, incidents);
                SA.setAlgorithm(algorithm);
                double score = SA.run().getScore();

                // Print the formatted double value
                System.out.print(df.format(score) + ",\t");
            }
        }

    }

    public static void sAFitnessPlot() {
        final String MAIN_PATH = "C:\\Users\\termi\\Documents\\Projects\\SchoolProjects\\SA-For-Post-Disaster" +
                "-Management\\src\\main\\resources\\";

        ProblemData.init(
                new File(MAIN_PATH + "Process Time and Capabilities.json"),
                new File(MAIN_PATH + "Incident Types and Locations.json"),
                new File(MAIN_PATH + "Unit Types.json"),
                new File(MAIN_PATH + "Distance Matrix.json"));

        SimulatedAnnealing SA = new SimulatedAnnealing(6000000, 0.001, ProblemData.INCIDENTS);
        SA.run();
    }
}