package fediverse.fediversesim.services.simulations;

import java.util.*;

import fediverse.fediversesim.model.FediverseHistory;
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
        FediverseHistory fediverseHistory = simulation.getFediverseHistory();

        int year = 2024;
        Fediverse lastState = fediverseHistory.getAllStates().get(0);
        lastState.setYear(year);
        this.displayResults(lastState);
        while (year <= 2034) {
            year++;
            Fediverse currentState = this.simulateYear(lastState);
            currentState.setYear(year);
            fediverseHistory.getAllStates().add(currentState);
            this.displayResults(currentState);
            lastState = currentState;
        }
    }

    public Fediverse simulateYear(Fediverse currentFediverseState) {
        Fediverse resultState = new Fediverse();
        resultState.getServers().addAll(currentFediverseState.getServers().stream().map(s -> new Server(s.getSimulationService(), s.getName(), s.getUsersPerMonth(), s.getId())).toList());

        List<Server> servers = currentFediverseState.getServers();

        // Calculate migration matrices
        MostLikelyMigrationMatrix mostLikelyMigrationMatrix = new MostLikelyMigrationMatrix();

        for (Server server : servers) {
            Map<Server, Double> mostLikelyMigrationDesintation = calculateMostLikelyMigrationMatrix(server, servers);
            mostLikelyMigrationMatrix.values.put(server, mostLikelyMigrationDesintation);
        }

        // Simulate user migration
        MigrationResultsMatrix migrationResultsMatrix = calculateMigrationResults(currentFediverseState, mostLikelyMigrationMatrix);

        // Do migration
        migrateUsers(resultState, migrationResultsMatrix);

        return resultState;
    }

    public Map<Server, Double> calculateMostLikelyMigrationMatrix(Server homeServer, List<Server> allServers) {
        Map<Server, Double> result = new HashMap<>();

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

    void migrateUsers(Fediverse fediverseState, MigrationResultsMatrix migrationResultsMatrix) {
        for (Map.Entry<Server, Map<Server, Long>> migrationEntry : migrationResultsMatrix.values.entrySet()) {
            Server homeServer = migrationEntry.getKey();
            for (Map.Entry<Server, Long> entry : migrationEntry.getValue().entrySet()) {
                Server destination = entry.getKey();
                Long migratingUsers = entry.getValue();
                homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() - migratingUsers);
                migrateUsersFromServerWithUuid(fediverseState, homeServer.getId(), migratingUsers);
                migrateUsersToServerWithUuid(fediverseState, destination.getId(), migratingUsers);
            }
        }
    }

    public void displayResults(Fediverse fediverse) {
        System.out.println("=== Year " + fediverse.getYear() + " ===");
        for (Server server : fediverse.getServers()) {
            System.out.println(server.toString());
        }
    }

    private void migrateUsersFromServerWithUuid(Fediverse fediverseState, String serverId, long numOfUsers) {
        Optional<Server> homeServerOptional = fediverseState.getServers().stream().filter(s -> s.getId().equals(serverId)).findFirst();

        if (homeServerOptional.isPresent()) {
            Server homeServer = homeServerOptional.get();
            homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() - numOfUsers);
        } else {
            throw new IllegalStateException("Did not find server with id '" + serverId + "'in Fediverse state");
        }
    }

    private void migrateUsersToServerWithUuid(Fediverse fediverseState, String serverId, long numOfUsers) {
        Optional<Server> homeServerOptional = fediverseState.getServers().stream().filter(s -> s.getId().equals(serverId)).findFirst();

        if (homeServerOptional.isPresent()) {
            Server homeServer = homeServerOptional.get();
            homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() + numOfUsers);
        } else {
            throw new IllegalStateException("Did not find server with id '" + serverId + "'in Fediverse state");
        }
    }
}
