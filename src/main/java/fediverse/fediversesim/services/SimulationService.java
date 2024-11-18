package fediverse.fediversesim.services;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Server;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class SimulationService {
    private Random random;

    public SimulationService() {
        this.random = new Random();
    }

    public void runSimulation(Fediverse fediverse) {
        int year = 2024;
        fediverse.setYear(year);
        this.displayResults(fediverse);
        while (year <= 2034) {
            this.simulateYear(fediverse);
            this.displayResults(fediverse);
            year++;
            fediverse.setYear(year);
        }
    }

    public void simulateYear(Fediverse fediverse) {
        List<Server> servers = fediverse.getServers();

        double totalUsers = 0;

        // Calculate overall capital
        for (Server server : servers) {
            totalUsers += server.getUsersPerMonth();
        }

        // Simulate user migration
        double migrationRate = random.nextDouble() * 100; // Random between 0% and 100%
        long usersMigratingTotal = (long) (totalUsers * migrationRate / 100);
        long usersMigratingLeft = usersMigratingTotal;

        // Distribute migrating users among servers
        while(usersMigratingLeft > 0) {
            // pick a random server, migrate users from there to another random server
            Server losingServer = getRandomNonEmptyServer(servers, null);
            Server winningServerB = getRandomNonEmptyServer(servers, losingServer);
            long newUsers = (long) (usersMigratingLeft * random.nextDouble());
            if (newUsers == 0) newUsers = 1;
            if (losingServer.getUsersPerMonth() < newUsers) {
                newUsers = losingServer.getUsersPerMonth();
            }
            usersMigratingLeft -= newUsers;
            losingServer.setUsersPerMonth(losingServer.getUsersPerMonth() - newUsers);
            winningServerB.setUsersPerMonth(winningServerB.getUsersPerMonth() + newUsers);
        }
    }

    private Server getRandomNonEmptyServer(List<Server> servers, Server exception) {
        List<Server> nonEmptyServers = servers.stream().filter(s -> s.getUsersPerMonth() > 0).toList();
        int randomServerId = (int)(random.nextDouble() * (nonEmptyServers.size() - 1));
        if (exception != null) {
            if (nonEmptyServers.get(randomServerId) == exception) randomServerId++;
            if (randomServerId >= nonEmptyServers.size()) randomServerId = 0;
        }
        return nonEmptyServers.get(randomServerId);
    }

    public void displayResults(Fediverse fediverse) {
        System.out.println("=== Year " + fediverse.getYear() + " ===");
        for (Server server : fediverse.getServers()) {
            System.out.println(server.toString());
        }
    }
}
