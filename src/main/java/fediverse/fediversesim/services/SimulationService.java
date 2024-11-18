package fediverse.fediversesim.services;

import fediverse.fediversesim.model.Fediverse;
import org.springframework.stereotype.Service;

@Service
public abstract class SimulationService {
    public String runSimulation(Fediverse fediverse) {
        return "";
    }
}
