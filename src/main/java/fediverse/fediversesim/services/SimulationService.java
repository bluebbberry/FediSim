package fediverse.fediversesim.services;

import fediverse.fediversesim.model.FediverseHistory;
import fediverse.fediversesim.model.FediverseState;
import fediverse.fediversesim.model.Simulation;
import org.springframework.stereotype.Service;

@Service
public abstract class SimulationService {
    public void runSimulation(Simulation simulation) {
        FediverseHistory fediverseHistory = simulation.getFediverseHistory();

        long startYear = simulation.getFediverseHistory().getAllStates().get(0).getYear();
        long currentYear = startYear;
        long simulationTime = 10;
        FediverseState lastState = fediverseHistory.getAllStates().get(0);
        lastState.setYear(currentYear);
        this.displayResults(lastState);
        while (currentYear <= startYear + simulationTime) {
            currentYear++;
            FediverseState currentState = this.simulateYear(lastState);
            currentState.setYear(currentYear);
            fediverseHistory.getAllStates().add(currentState);
            this.displayResults(currentState);
            lastState = currentState;
        }
    }

    protected abstract void displayResults(FediverseState currentState);

    public abstract FediverseState simulateYear(FediverseState fediverseState);
}
