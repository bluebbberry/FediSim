package fediverse.fediversesim.controllers;

import fediverse.fediversesim.model.FediverseState;
import fediverse.fediversesim.model.FediverseHistory;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.services.simulations.SimpleSimulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
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
    ResponseEntity<HashMap<String, String>> createSimulation(@RequestBody FediverseState fediverseState) {
        Simulation simulation = new Simulation(fediverseState);
        simulationList.add(simulation);
        log.info("Created new simulation with id '{}'", simulation.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("id", simulation.getId());
        return ResponseEntity.ok().body(response);
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
    ResponseEntity<HashMap<String, FediverseHistory>> getSimulation(@PathVariable String id) {
        Optional<Simulation> simulationOptional = simulationList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (simulationOptional.isPresent()) {
            Simulation simulation = simulationOptional.get();
            HashMap<String, FediverseHistory> response = new HashMap<>();
            response.put("simulation", simulation.getFediverseHistory());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
