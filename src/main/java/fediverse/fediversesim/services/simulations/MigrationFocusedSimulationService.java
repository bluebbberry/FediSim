package fediverse.fediversesim.services.simulations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;

class MostLikelyMigrationMatrix {
    public Map<Server, Map<Server, Long>> values;
}

class MigrationResultsMatrix {
    public Map<Server, Map<Server, Long>> values;
}

@Service
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
            Map<Server, Long> mostLikelyMigrationDesintation = calculateMostLikelyMigrationMatrix(server, servers.stream().filter(s -> s != server).toList());
            mostLikelyMigrationMatrix.values.put(server, mostLikelyMigrationDesintation);
        }

        // Simulate user migration
        MigrationResultsMatrix migrationResultsMatrix = calculateMigrationResults(fediverse, mostLikelyMigrationMatrix);

        // Do migration
        migrateUsers(fediverse, migrationResultsMatrix);
    }

    public HashMap<Server, Long> calculateMostLikelyMigrationMatrix(Server homeServer, List<Server> otherServers) {
        // TODO
        return null;
    }

    MigrationResultsMatrix calculateMigrationResults(Fediverse fediverse, MostLikelyMigrationMatrix mostLikelyMigrationMatrix) {
        MigrationResultsMatrix migrationResultsMatrix = new MigrationResultsMatrix();

        for (Server server : mostLikelyMigrationMatrix.values.keySet()) {
            migrationResultsMatrix.values.put(server, 0L);
        }

        for (Map.Entry<Server, Map<Server, Long>> entry : mostLikelyMigrationMatrix.values.entrySet()) {
            Server homeServer = entry.getKey();
            Map<Server, Long> destPairs = entry.getValue();
            migrationResultsMatrix.values.put(homeServer, calculateMigrationResultsForServer(homeServer, destPairs));
        }

        return migrationResultsMatrix;
    }

    Map<Server, Long> calculateMigrationResultsForServer(Server server, Map<Server, Long> mostLikelyMigrationMatrix) {
        Map<Server, Long> results = new HashMap<>();
        for (Map.Entry<Server, Long> entry : mostLikelyMigrationMatrix.entrySet()) {
            Server desination = entry.getKey();
            Long probability = entry.getValue();
            results.put(desination, server.getUsersPerMonth() * probability);
        }
        return results;
    }

    void migrateUsers(Fediverse fediverse, MigrationResultsMatrix migrationResultsMatrix) {
        for (Server homeServer : fediverse.getServers()) {
            for (Map.Entry<Server, Long> entry : migrationResultsMatrix.values.get(homeServer).entrySet()) {
                Server desination = entry.getKey();
                Long migratingUsers = entry.getValue();
                homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() - migratingUsers);
                desination.setUsersPerMonth(desination.getUsersPerMonth() + migratingUsers);
            }
        }
    }

    public void displayResults(Fediverse fediverse) {
        System.out.println("=== Year " + fediverse.getYear() + " ===");
        for (Server server : fediverse.getServers()) {
            System.out.println(server.toString());
        }
    }
}
