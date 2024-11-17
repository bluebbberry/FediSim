package fediverse.fediversesim.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationManager {
    private List<SocialMediaServer> servers;
    private Random random;

    public SimulationManager() {
        this.servers = new ArrayList<>();
        this.random = new Random();
    }

    public void addServer(SocialMediaServer server) {
        servers.add(server);
    }

    public List<SocialMediaServer> getServers() {
        return servers;
    }

    public void simulateYear(int year) {
        double totalUsers = 0;
        double totalCapital = 0;

        // Distribute resources among servers
        for (SocialMediaServer server : servers) {
            server.distributeResources(totalUsers + server.getUsersPerMonth(), totalCapital + server.getEconomicCapital());
            totalUsers += server.getUsersPerMonth();
            totalCapital += server.getEconomicCapital();
        }

        // Simulate user growth
        double growthRate = random.nextDouble() * 100; // Random between 0% and 100%
        totalUsers *= (1 + growthRate / 100);

        // Distribute new users among servers
        for (SocialMediaServer server : servers) {
            int newUsers = (int) ((server.getUsersPerMonth() / totalUsers) * totalUsers);
            server.setUsersPerMonth(newUsers);
        }

        // Simulate economic capital growth
        double capitalGrowthRate = random.nextDouble() * 100; // Random between 0% and 100%
        totalCapital *= (1 + capitalGrowthRate / 100);

        // Distribute new capital among servers
        for (SocialMediaServer server : servers) {
            double newCapital = (server.getEconomicCapital() / totalCapital) * totalCapital;
            server.setEconomicCapital(newCapital);
        }
    }

    public void displayResults(int year) {
        System.out.println("=== Year " + year + " ===");
        for (SocialMediaServer server : servers) {
            System.out.println(server.toString());
        }
    }
}
