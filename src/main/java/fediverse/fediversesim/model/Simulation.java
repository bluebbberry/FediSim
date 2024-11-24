package fediverse.fediversesim.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Simulation {
    private FediverseHistory fediverseHistory;
    private String id;

    public Simulation(FediverseState startState) {
        this.fediverseHistory = new FediverseHistory();
        fediverseHistory.fediverseStates.add(startState);
        this.id = UUID.randomUUID().toString();
    }
}
