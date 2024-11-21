package fediverse.fediversesim.controllers;

import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.model.FediverseHistory;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.services.simulations.SimpleSimulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/simulations")
@Slf4j
public class SimulationController {

    SimulationService simulationService;
    List<Simulation> simulationList = new ArrayList<>();

    public SimulationController(SimpleSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/create")
    ResponseEntity<String> createSimulation(@RequestBody Fediverse fediverse) {
        Simulation simulation = new Simulation(fediverse);
        simulationList.add(simulation);
        log.info("Created new simulation with id '{}'", simulation.getId());
        return ResponseEntity.ok().body(simulation.getId());
    }

    @GetMapping("/start/{id}")
    ResponseEntity<String> startSimulation(@PathVariable String id) {
        Optional<Simulation> simulationOptional = simulationList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (simulationOptional.isPresent()) {
            log.info("Start simulation");
            Simulation simulation = simulationOptional.get();
            simulationService.runSimulation(simulation);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<FediverseHistory> getSimulation(@PathVariable String id) {
        Optional<Simulation> simulationOptional = simulationList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (simulationOptional.isPresent()) {
            Simulation simulation = simulationOptional.get();
            return ResponseEntity.ok().body(simulation.getFediverseHistory());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
