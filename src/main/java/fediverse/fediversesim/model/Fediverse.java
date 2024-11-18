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

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(this.year);
        this.servers.forEach(s -> {
            build.append(" ");
            build.append(s.toString());
        });
        return build.toString();
    }
}
