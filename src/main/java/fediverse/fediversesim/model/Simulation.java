package fediverse.fediversesim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class Simulation {
    @JsonProperty("startState")
    private FediverseState startState;
    @JsonProperty("type")
    private String type;
    @JsonProperty("duration")
    private long duration;

    @JsonIgnore
    private FediverseHistory fediverseHistory;
    @JsonIgnore
    private String id;

    public Simulation(FediverseState startState, String type, long duration) {
        this.fediverseHistory = new FediverseHistory();
        fediverseHistory.fediverseStates.add(startState);
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.duration = duration;
    }
}
