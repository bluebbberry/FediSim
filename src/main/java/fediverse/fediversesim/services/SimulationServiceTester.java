package fediverse.fediversesim.services;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class SimulationServiceTester {
    Map<String, String> statsFiles = Map.of(
        "Lemmy", "lemmy-active-users.csv"
    );

    public void runSimulationsAndCompareWithActualStats() {
        this.readStatsFiles();
    }

    private void readStatsFiles() {
        for (Map.Entry<String, String> statFile : statsFiles.entrySet()) {
            Properties properties = new Properties();
            try {
                File file = ResourceUtils.getFile("classpath:" + statFile.getValue());
                InputStream in = new FileInputStream(file);
                properties.load(in);
                System.out.println(properties.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
