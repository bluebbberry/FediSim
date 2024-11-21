package fediverse.fediversesim.services;

import fediverse.fediversesim.model.FediverseHistory;
import fediverse.fediversesim.model.FediverseState;
import fediverse.fediversesim.model.Simulation;
import org.springframework.stereotype.Service;

@Service
public abstract class SimulationService {
    public void runSimulation(Simulation simulation) {
        FediverseHistory fediverseHistory = simulation.getFediverseHistory();

        int year = 2024;
        FediverseState lastState = fediverseHistory.getAllStates().get(0);
        lastState.setYear(year);
        this.displayResults(lastState);
        while (year <= 2034) {
            year++;
            FediverseState currentState = this.simulateYear(lastState);
            currentState.setYear(year);
            fediverseHistory.getAllStates().add(currentState);
            this.displayResults(currentState);
            lastState = currentState;
        }
    }

    protected abstract void displayResults(FediverseState currentState);

    public abstract FediverseState simulateYear(FediverseState fediverseState);
}
