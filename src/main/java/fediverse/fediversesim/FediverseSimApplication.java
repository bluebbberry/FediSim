package fediverse.fediversesim;

import fediverse.fediversesim.model.SimulationManager;
import fediverse.fediversesim.model.SocialMediaServer;
import fediverse.fediversesim.model.SocialMediaSimulation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FediverseSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(FediverseSimApplication.class, args);

		SimulationManager simulationManager = new SimulationManager();

		SocialMediaSimulation simulation = new SocialMediaSimulation();

		// Create sample servers
		simulation.getSimulationManager().addServer(new SocialMediaServer(simulationManager, "Server A", 1000000, 10000));
		simulation.getSimulationManager().addServer(new SocialMediaServer(simulationManager, "Server B", 1500000, 15000));
		simulation.getSimulationManager().addServer(new SocialMediaServer(simulationManager, "Server C", 2000000, 20000));

		simulation.runSimulation();
	}

}

