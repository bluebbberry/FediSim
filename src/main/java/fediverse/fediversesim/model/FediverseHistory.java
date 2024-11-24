package fediverse.fediversesim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FediverseHistory {
    @JsonProperty("fediverseStates")
    List<FediverseState> fediverseStates = new ArrayList<>();
}
