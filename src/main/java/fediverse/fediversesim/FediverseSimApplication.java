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

		FediverseSimulation simulation = new FediverseSimulation();

		SimulationManager simulationManager = simulation.getSimulationManager();

		// Create sample servers
		simulation.getSimulationManager().addServer(new FediverseServer(
				simulationManager,
				"Lemmy",
				1000,
				1000,
				300));
		simulation.getSimulationManager().addServer(new FediverseServer(
				simulationManager,
				"Bluesky",
				1000,
				4000,
				300));
		simulation.getSimulationManager().addServer(new FediverseServer(simulationManager,
				"Mastodon",
				15000,
				15000,
				300));
		simulation.getSimulationManager().addServer(new FediverseServer(simulationManager,
				"Threads",
				2000000,
				20000,
				300));

		simulation.runSimulation();
	}
}
