package fediverse.fediversesim.controllers;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.services.SimulationService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/simulate")
    ResponseEntity<String> simulate(@RequestBody Fediverse fediverse) {
        String result = simulationService.runSimulation(fediverse);
        return ResponseEntity.ok().body(result);
    }
}
