package fediverse.fediversesim;

import fediverse.fediversesim.model.SimulationManager;
import fediverse.fediversesim.model.FediverseServer;
import fediverse.fediversesim.model.FediverseSimulation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FediverseSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(FediverseSimApplication.class, args);

		SimulationManager simulationManager = new SimulationManager();

		FediverseSimulation simulation = new FediverseSimulation();

		// Create sample servers
		simulation.getSimulationManager().addServer(new FediverseServer(simulationManager, "Server A", 1000000, 10000));
		simulation.getSimulationManager().addServer(new FediverseServer(simulationManager, "Server B", 1500000, 15000));
		simulation.getSimulationManager().addServer(new FediverseServer(simulationManager, "Server C", 2000000, 20000));

		simulation.runSimulation();
	}

}

