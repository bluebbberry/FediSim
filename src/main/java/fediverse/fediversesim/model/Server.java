package fediverse.fediversesim.model;

import fediverse.fediversesim.services.SimulationService;
import lombok.Data;

@Data
public class Server {
    private String name;
    private double economicCapital;
    // social capital
    private long usersPerMonth;
    private double computationalCapital;
    // federation (TODO: add degree of federation to all other servers)
    private double migrationCost;
    // content
    private double contentLength;
    private double contentImmersion;
    private double leftLeaningPopulation;
    private double liberalismLeaningPopulation;
    private double conservativeLeaningPopulation;
    private SimulationService simulationService;

    public Server(SimulationService simulationService, String name, double initialEconomicCapital, int initialUsersPerMonth, double initialComputationCapital) {
        this.simulationService = simulationService;
        this.name = name;
        this.economicCapital = initialEconomicCapital;
        this.usersPerMonth = initialUsersPerMonth;
        this.computationalCapital = initialComputationCapital;
    }

    @Override
    public String toString() {
        return this.name + " | " + this.economicCapital + "€ | " + this.usersPerMonth + " MAU";
    }
}
