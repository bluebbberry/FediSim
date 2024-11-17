package fediverse.fediversesim.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class SimulationManager {
    private final List<FediverseServer> servers;
    private final Random random;

    public SimulationManager() {
        this.servers = new ArrayList<>();
        this.random = new Random();
    }

    public void addServer(FediverseServer server) {
        servers.add(server);
    }

    public void simulateYear(int year) {
        double totalUsers = 0;

        // Calculate overall capital
        for (FediverseServer server : servers) {
            totalUsers += server.getUsersPerMonth();
        }

        // Simulate user migration
        double migrationRate = random.nextDouble() * 100; // Random between 0% and 100%
        long usersMigratingTotal = (long) (totalUsers * migrationRate / 100);
        long usersMigratingLeft = usersMigratingTotal;

        // Distribute migrating users among servers
        while(usersMigratingLeft > 0) {
            // pick a random server, migrate users from there to another random server
            FediverseServer losingFediverseServer = getRandomNonEmptyServer(null);
            FediverseServer winningFediverseServerB = getRandomNonEmptyServer(losingFediverseServer);
            long newUsers = (long) (usersMigratingLeft * random.nextDouble());
            if (newUsers == 0) newUsers = 1;
            if (losingFediverseServer.getUsersPerMonth() < newUsers) {
                newUsers = losingFediverseServer.getUsersPerMonth();
            }
            usersMigratingLeft -= newUsers;
            losingFediverseServer.setUsersPerMonth(losingFediverseServer.getUsersPerMonth() - newUsers);
            winningFediverseServerB.setUsersPerMonth(winningFediverseServerB.getUsersPerMonth() + newUsers);
        }
    }

    private FediverseServer getRandomNonEmptyServer(FediverseServer exception) {
        List<FediverseServer> nonEmptyServers = servers.stream().filter(s -> s.getUsersPerMonth() > 0).toList();
        int randomServerId = (int)(random.nextDouble() * (nonEmptyServers.size() - 1));
        if (exception != null) {
            if (nonEmptyServers.get(randomServerId) == exception) randomServerId++;
            if (randomServerId >= nonEmptyServers.size()) randomServerId = 0;
        }
        return nonEmptyServers.get(randomServerId);
    }

    public void displayResults(int year) {
        System.out.println("=== Year " + year + " ===");
        for (FediverseServer server : servers) {
            System.out.println(server.toString());
        }
    }
}
