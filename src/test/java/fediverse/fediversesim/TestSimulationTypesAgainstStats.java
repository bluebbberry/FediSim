package fediverse.fediversesim;

import fediverse.fediversesim.services.SimulationServiceTester;
import org.junit.jupiter.api.Test;

public class TestSimulationTypesAgainstStats {
    @Test
    void test() {
        SimulationServiceTester simulationServiceTester = new SimulationServiceTester();
        simulationServiceTester.runSimulationsAndCompareWithActualStats();
    }
}
