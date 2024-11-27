package fediverse.fediversesim;

import fediverse.fediversesim.model.FediverseState;
import fediverse.fediversesim.model.Server;
import fediverse.fediversesim.model.Simulation;
import fediverse.fediversesim.services.SimulationService;
import fediverse.fediversesim.services.simulations.MigrationFocusedSimulationService;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MigrationBasedSimulationTests {

    @Test
    void test() {
        SimulationService simulationService = new MigrationFocusedSimulationService();

        FediverseState fediverseState = new FediverseState();

        // Create sample servers
        Server lemmyServer = new Server(
                simulationService,
                "Lemmy",
                1000);
        fediverseState.getServers().add(lemmyServer);
        Server bskyServer = new Server(
                simulationService,
                "Bluesky",
                4000);
        fediverseState.getServers().add(bskyServer);
        fediverseState.getServers().add(new Server(simulationService,
                "Mastodon",
                15000));
        fediverseState.getServers().add(new Server(simulationService,
                "Threads",
                20000));
        Simulation simulation = new Simulation(fediverseState, "simple", 10);
        simulationService.runSimulation(simulation);

        Assert.assertTrue(simulation.getStartState().getServers().stream().filter(s -> s.getName().equals("Lemmy")).findFirst().get().getUsersPerMonth() == 1000);
        Assert.assertTrue(simulation.getStartState().getServers().stream().filter(s -> s.getName().equals("Bluesky")).findFirst().get().getUsersPerMonth() == 4000);
        Assert.assertTrue(simulation.getStartState().getServers().stream().filter(s -> s.getName().equals("Mastodon")).findFirst().get().getUsersPerMonth() == 15000);
        Assert.assertTrue(simulation.getStartState().getServers().stream().filter(s -> s.getName().equals("Threads")).findFirst().get().getUsersPerMonth() == 20000);
    }
}
