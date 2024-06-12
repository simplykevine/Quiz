import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class TopNIPAddresses {
    
    static class IPAddressCount implements Comparable<IPAddressCount> {
        String ipAddress;
        int count;

        public IPAddressCount(String ipAddress, int count) {
            this.ipAddress = ipAddress;
            this.count = count;
        }

        @Override
        public int compareTo(IPAddressCount other) {
            // Compare counts in descending order
            return Integer.compare(other.count, this.count);
        }
    }

    public static void main(String[] args) {
        String logFilePath = "sample_01_easy.log";
        int n = 3;

        try {
            Map<String, Integer> ipCounts = readLog(logFilePath);
            PriorityQueue<IPAddressCount> topNIPs = findTopNIPs(ipCounts, n);
            writeResult(topNIPs, n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> readLog(String logFilePath) throws IOException {
        Map<String, Integer> ipCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String ipAddress = parts[0].trim();
                int count = Integer.parseInt(parts[1].trim());
                ipCounts.put(ipAddress, ipCounts.getOrDefault(ipAddress, 0) + count);
            }
        }
        return ipCounts;
    }

    private static PriorityQueue<IPAddressCount> findTopNIPs(Map<String, Integer> ipCounts, int n) {
        PriorityQueue<IPAddressCount> topNIPs = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : ipCounts.entrySet()) {
            IPAddressCount ipCount = new IPAddressCount(entry.getKey(), entry.getValue());
            topNIPs.offer(ipCount);
            if (topNIPs.size() > n) {
                topNIPs.poll(); // Remove the smallest count IP address
            }
        }
        return topNIPs;
    }

    private static void writeResult(PriorityQueue<IPAddressCount> topNIPs, int n) throws IOException {
        try (FileWriter writer = new FileWriter("output_top_" + n + "_ips.txt")) {
            while (!topNIPs.isEmpty()) {
                IPAddressCount ipCount = topNIPs.poll();
                writer.write(ipCount.count + ", " + ipCount.ipAddress + "\n");
            }
        }
    }
}
