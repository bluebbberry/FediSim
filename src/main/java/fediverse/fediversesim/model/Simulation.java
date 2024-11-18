package fediverse.fediversesim.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Simulation {
    private Fediverse fediverse;
    private String id;
    private List<String> result;

    public Simulation(Fediverse fediverse) {
        this.fediverse = fediverse;
        this.id = UUID.randomUUID().toString();
    }
}
