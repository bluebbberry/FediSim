package fediverse.fediversesim.services;

import fediverse.fediversesim.model.Simulation;
import org.springframework.stereotype.Service;

@Service
public abstract class SimulationService {
    public void runSimulation(Simulation simulation) {}
}
