package fediverse.fediversesim;

import fediverse.fediversesim.model.FediverseState;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.services.simulations.MigrationFocusedSimulationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FediverseSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(FediverseSimApplication.class, args);

		SimulationService simulationService = new MigrationFocusedSimulationService();

		FediverseState fediverseState = new FediverseState();

		// Create sample servers
		fediverseState.getServers().add(new Server(
				simulationService,
				"Lemmy",
				 1000));
		fediverseState.getServers().add(new Server(
				simulationService,
				"Bluesky",
				 4000));
		fediverseState.getServers().add(new Server(simulationService,
				"Mastodon",
				 15000));
		fediverseState.getServers().add(new Server(simulationService,
				"Threads",
				20000));
		simulationService.runSimulation(new Simulation(fediverseState));
	}
}
