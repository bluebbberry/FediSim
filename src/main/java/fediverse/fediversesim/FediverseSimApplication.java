package fediverse.fediversesim;

import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Fediverse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FediverseSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(FediverseSimApplication.class, args);

		SimulationService simulationService = new SimulationService();

		Fediverse fediverse = new Fediverse();

		// Create sample servers
		fediverse.getServers().add(new Server(
				simulationService,
				"Lemmy",
				1000,
				1000,
				300));
		fediverse.getServers().add(new Server(
				simulationService,
				"Bluesky",
				1000,
				4000,
				300));
		fediverse.getServers().add(new Server(simulationService,
				"Mastodon",
				15000,
				15000,
				300));
		fediverse.getServers().add(new Server(simulationService,
				"Threads",
				2000000,
				20000,
				300));

		simulationService.runSimulation(fediverse);
	}
}
