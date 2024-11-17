package fediverse.fediversesim.model;

import lombok.Data;

@Data
public class FediverseServer {
    private String name;
    private double economicCapital;
    // social capital
    private long usersPerMonth;
    private double computationalCapital;
    private SimulationManager simulationManager;

    public FediverseServer(SimulationManager simulationManager, String name, double initialEconomicCapital, int initialUsersPerMonth, double initialComputationCapital) {
        this.simulationManager = simulationManager;
        this.name = name;
        this.economicCapital = initialEconomicCapital;
        this.usersPerMonth = initialUsersPerMonth;
        this.computationalCapital = initialComputationCapital;
    }

    public void distributeResources(double totalUsers, double totalCapital) {
        economicCapital = totalCapital / (double) simulationManager.getServers().size();
        usersPerMonth = (int) (totalUsers / (double) simulationManager.getServers().size());
    }

    @Override
    public String toString() {
        return this.name + " | " + this.economicCapital + "€ | " + this.usersPerMonth + " MAU";
    }
}
