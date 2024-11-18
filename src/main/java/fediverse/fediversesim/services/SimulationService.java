package fediverse.fediversesim.services;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.Simulation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class SimulationService {
    public void runSimulation(Simulation simulation) {}
}
