package fediverse.fediversesim.services.simulations;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class MostLikelyMigrationMatrix {
    public HashMap<Server, HashMap<Server, Long>> values;
}

class MigrationResultsMatrix {
    public HashMap<Server, Long> values;
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
            HashMap<Server, Long> mostLikelyMigrationDesintation = calculateMostLikelyMigrationMatrix(server, servers.stream().filter(s -> s != server).toList());
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
        // TODO
        return null;
    }

    void migrateUsers(Fediverse fediverse, MigrationResultsMatrix migrationResultsMatrix) {
        // TODO
    }

    public void displayResults(Fediverse fediverse) {
        System.out.println("=== Year " + fediverse.getYear() + " ===");
        for (Server server : fediverse.getServers()) {
            System.out.println(server.toString());
        }
    }
}
