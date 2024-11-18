package fediverse.fediversesim.services.simulations;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SimpleSimulationService extends SimulationService {
    private final Random random;

    public SimpleSimulationService() {
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
