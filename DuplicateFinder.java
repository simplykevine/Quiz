import java.io.*;

public class DuplicateFinder {
    public static void main(String[] args) {
        String inputFilePath = "sample_data/test_01.txt"; 
        String outputFilePath = "sample_results/output.txt"; 
        System.out.println("Input File Path: " + inputFilePath);
        System.out.println("Output File Path: " + outputFilePath);
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            System.err.println("Input file does not exist: " + inputFilePath);
            return;
        }
        File outputDir = new File("sample_results");
        if (!outputDir.exists()) {
            System.err.println("Output directory does not exist: " + outputDir.getAbsolutePath());
            return;
        }

        try {
            int[] duplicates = readAndProcessInput(inputFilePath);
            writeOutput(outputFilePath, duplicates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] readAndProcessInput(String filePath) throws IOException {
        boolean[] seen = new boolean[2047]; 
        boolean[] duplicates = new boolean[2047];
        int[] duplicatesList = new int[2047];
        int count = 0;

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.split("\\s+").length > 1) {
                continue;
            }
            try {
                int number = Integer.parseInt(line);
                int index = number + 1023; 
                if (seen[index]) {
                    if (!duplicates[index]) {
                        duplicates[index] = true;
                        duplicatesList[count++] = number;
                    }
                } else {
                    seen[index] = true;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
        reader.close();

        int[] result = new int[count];
        System.arraycopy(duplicatesList, 0, result, 0, count);
        sort(result);
        return result;
    }

    private static void sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static void writeOutput(String filePath, int[] duplicates) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (int number : duplicates) {
            writer.write(Integer.toString(number));
            writer.newLine();
        }
        writer.close();
    }
}
