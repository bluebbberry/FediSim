package fediverse.fediversesim.services.simulations;

import java.util.*;

import fediverse.fediversesim.model.FediverseState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import fediverse.fediversesim.model.Server;
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

    public FediverseState simulateYear(FediverseState currentFediverseState) {
        FediverseState resultState = new FediverseState();
        resultState.getServers().addAll(currentFediverseState.getServers().stream().map(Server::new).toList());

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

    // Source: http://www.eecs.qmul.ac.uk/~tysong/files/Migration-IMC23.pdf
    // "A unique feature of Mastodon is that users can easily ‘switch’ in-
    //stance. This involves migrating their data from one instance to
    //another. We are curious to see if this is also driven by network ef-
    //fects. Overall, 4.09% of the users have switched from the Mastodon
    //instance they initially created an account on (hereinafter first in-
    //stance) to a new instance (hereinafter second instance). Curiously,
    //97.22% of these switches happened after Musk’s Twitter takeover.
    //This suggests that some users have backtracked on their instance
    //choices, perhaps after finding a more suitable one.
    // --> 4% seems to be somewhat of a maximum"
    public Map<Server, Double> calculateMostLikelyMigrationMatrix(Server homeServer, List<Server> allServers) {
        Map<Server, Double> result = new HashMap<>();

        for (Server otherServer : allServers) {
            double percentageThatMigrates = this.random.nextDouble() * 0.04;
            if (otherServer != homeServer) {
                result.put(otherServer, percentageThatMigrates / allServers.size());
            } else {
                result.put(otherServer, 1 - percentageThatMigrates);
            }
        }

        return result;
    }

    MigrationResultsMatrix calculateMigrationResults(FediverseState fediverseState, MostLikelyMigrationMatrix mostLikelyMigrationMatrix) {
        MigrationResultsMatrix migrationResultsMatrix = new MigrationResultsMatrix();

        for (Server server : fediverseState.getServers()) {
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

    void migrateUsers(FediverseState fediverseState, MigrationResultsMatrix migrationResultsMatrix) {
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

    public void displayResults(FediverseState fediverseState) {
        System.out.println("=== Year " + fediverseState.getYear() + " ===");
        for (Server server : fediverseState.getServers()) {
            System.out.println(server.toString());
        }
    }

    private void migrateUsersFromServerWithUuid(FediverseState fediverseState, String serverId, long numOfUsers) {
        Optional<Server> homeServerOptional = fediverseState.getServers().stream().filter(s -> s.getId().equals(serverId)).findFirst();

        if (homeServerOptional.isPresent()) {
            Server homeServer = homeServerOptional.get();
            homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() - numOfUsers);
        } else {
            throw new IllegalStateException("Did not find server with id '" + serverId + "'in Fediverse state");
        }
    }

    private void migrateUsersToServerWithUuid(FediverseState fediverseState, String serverId, long numOfUsers) {
        Optional<Server> homeServerOptional = fediverseState.getServers().stream().filter(s -> s.getId().equals(serverId)).findFirst();

        if (homeServerOptional.isPresent()) {
            Server homeServer = homeServerOptional.get();
            homeServer.setUsersPerMonth(homeServer.getUsersPerMonth() + numOfUsers);
        } else {
            throw new IllegalStateException("Did not find server with id '" + serverId + "'in Fediverse state");
        }
    }
}
