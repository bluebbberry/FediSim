package fediverse.fediversesim.services.simulations;

import fediverse.fediversesim.services.SimulationService;

import java.util.HashMap;
import java.util.Map;

public class SimulationTypeManager {

    private static final Map<String, SimulationService> allSimulationTypes;

    static {
        allSimulationTypes = new HashMap<>();
        SimulationTypeManager.allSimulationTypes.put("simple", new SimpleSimulationService());
        SimulationTypeManager.allSimulationTypes.put("migration-based", new MigrationFocusedSimulationService());
    }

    public static SimulationService getByName(String name) {
        return allSimulationTypes.get(name);
    }

    public static String[] getAll() {
        return allSimulationTypes.keySet().toArray(new String[0]);
    }
}
