package fediverse.fediversesim;

import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Fediverse;
import fediverse.fediversesim.services.simulations.MigrationFocusedSimulationService;
import fediverse.fediversesim.services.simulations.SimpleSimulationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FediverseSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(FediverseSimApplication.class, args);

		SimulationService simulationService = new MigrationFocusedSimulationService();

		Fediverse fediverse = new Fediverse();

		// Create sample servers
		fediverse.getServers().add(new Server(
				simulationService,
				"Lemmy",
				 1000,
				null));
		fediverse.getServers().add(new Server(
				simulationService,
				"Bluesky",
				 4000,
				null
				));
		fediverse.getServers().add(new Server(simulationService,
				"Mastodon",
				 15000,
				null
				));
		fediverse.getServers().add(new Server(simulationService,
				"Threads",
				20000,
				null));
		simulationService.runSimulation(new Simulation(fediverse));
	}
}
