package fediverse.fediversesim.services.simulations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;

class MostLikelyMigrationMatrix {
    public Map<Server, Map<Server, Double>> values = new HashMap<>();
}

class MigrationResultsMatrix {
    public Map<Server, Map<Server, Long>> values = new HashMap<>();
}

@Service
@Slf4j
public class MigrationFocusedSimulationService extends SimulationService {
    private final Random random;

    public MigrationFocusedSimulationService() {
        this.random = new Random();
    }

    public void runSimulation(Simulation simulation) {
        List<String> result = new ArrayList<>();
        Fediverse fediverse = simulation.getFediverse();

        int year = 2024;
        fediverse.setYear(year);
        this.displayResults(fediverse);
        result.add(fediverse.toString());
        while (year <= 2034) {
            year++;
            fediverse.setYear(year);
            this.simulateYear(fediverse);
            this.displayResults(fediverse);
            result.add(fediverse.toString());
        }

        simulation.setResult(result);
    }

    public void simulateYear(Fediverse fediverse) {
        List<Server> servers = fediverse.getServers();

        // Calculate migration matrices
        MostLikelyMigrationMatrix mostLikelyMigrationMatrix = new MostLikelyMigrationMatrix();

        for (Server server : servers) {
            Map<Server, Double> mostLikelyMigrationDesintation = calculateMostLikelyMigrationMatrix(server, servers);
            mostLikelyMigrationMatrix.values.put(server, mostLikelyMigrationDesintation);
        }

        // Simulate user migration
        MigrationResultsMatrix migrationResultsMatrix = calculateMigrationResults(fediverse, mostLikelyMigrationMatrix);

        // Do migration
        migrateUsers(fediverse, migrationResultsMatrix);
    }

    public HashMap<Server, Double> calculateMostLikelyMigrationMatrix(Server homeServer, List<Server> allServers) {
        HashMap<Server, Double> result = new HashMap<>();

        for (Server otherServer : allServers) {
            if (otherServer != homeServer) {
                result.put(otherServer, 0.8 / allServers.size());
            } else {
                result.put(otherServer, 0.8);
            }
        }

        return result;
    }

    MigrationResultsMatrix calculateMigrationResults(Fediverse fediverse, MostLikelyMigrationMatrix mostLikelyMigrationMatrix) {
        MigrationResultsMatrix migrationResultsMatrix = new MigrationResultsMatrix();

        for (Server server : fediverse.getServers()) {
            Map<Server, Long> migrationResultEntry = new HashMap<>();
            migrationResultEntry.put(server, 0L);
            migrationResultsMatrix.values.put(server, migrationResultEntry);
        }

        for (Map.Entry<Server, Map<Server, Double>> entry : mostLikelyMigrationMatrix.values.entrySet()) {
            Server homeServer = entry.getKey();
            Map<Server, Double> destPairs = entry.getValue();
            migrationResultsMatrix.values.put(homeServer, calculateMigrationResultsForServer(homeServer, destPairs));
        }

        return migrationResultsMatrix;
    }

    Map<Server, Long> calculateMigrationResultsForServer(Server server, Map<Server, Double> mostLikelyMigrationMatrix) {
        Map<Server, Long> results = new HashMap<>();
        for (Map.Entry<Server, Double> entry : mostLikelyMigrationMatrix.entrySet()) {
            Server destination = entry.getKey();
            Double probability = entry.getValue();
            results.put(destination, (long)(server.getUsersPerMonth() * probability));
        }
        return results;
    }

    void migrateUsers(Fediverse fediverse, MigrationResultsMatrix migrationResultsMatrix) {
        fediverse.getServers().forEach(homeServer -> {
            Map<Server, Long> result = migrationResultsMatrix.values.get(homeServer);
            if (result != null) {
                for (Map.Entry<Server, Long> entry : result.entrySet()) {
                    Server desination = entry.getKey();
                    Long migratingUsers = entry.getValue();
                    homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() - migratingUsers);
                    desination.setUsersPerMonth(desination.getUsersPerMonth() + migratingUsers);
                }
            } else {
                log.info("No migration result for server {}", homeServer.toString());
            }
        });
    }

    public void displayResults(Fediverse fediverse) {
        System.out.println("=== Year " + fediverse.getYear() + " ===");
        for (Server server : fediverse.getServers()) {
            System.out.println(server.toString());
        }
    }
}
