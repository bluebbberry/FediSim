package fediverse.fediversesim.model;

import lombok.Data;

@Data
public class FediverseSimulation {
    private SimulationManager simulationManager;

    public FediverseSimulation() {
        this.simulationManager = new SimulationManager();
    }

    public void runSimulation() {
        int year = 2024;
        while (year <= 2034) {
            simulationManager.simulateYear(year);
            simulationManager.displayResults(year);
            year++;
        }
    }
}
