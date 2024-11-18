package fediverse.fediversesim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Fediverse {
    @JsonProperty("servers")
    private final List<Server> servers;
    @JsonProperty("year")
    private long year;

    public Fediverse() {
        this.servers = new ArrayList<>();
    }
}
