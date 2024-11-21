package fediverse.fediversesim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fediverse.fediversesim.services.SimulationService;
import lombok.Data;

import java.util.UUID;

@Data
public class Server {
    @JsonProperty("name")
    private String name;
//    private double economicCapital;
    // social capital
    @JsonProperty("usersPerMonth")
    private long usersPerMonth;
    @JsonProperty
    private String id;
    private SimulationService simulationService;
//    private double computationalCapital;
//    // federation (TODO: add degree of federation to all other servers)
//    private double migrationCost;
//    // content
//    private double contentLength;
//    private double contentImmersion;
//    private double leftLeaningPopulation;
//    private double liberalismLeaningPopulation;
//    private double conservativeLeaningPopulation;
//    private SimulationService simulationService;

    public Server(SimulationService simulationService, String name, long initialUsersPerMonth, String id) {
        this.simulationService = simulationService;
        this.name = name;
//        this.economicCapital = initialEconomicCapital;
        this.usersPerMonth = initialUsersPerMonth;
//        this.computationalCapital = initialComputationCapital;
        if (id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return this.name + " | " + this.usersPerMonth + " MAU";
    }
}
