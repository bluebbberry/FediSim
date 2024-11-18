package fediverse.fediversesim.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Fediverse {
    private final List<Server> servers;
    private long year;

    public Fediverse() {
        this.servers = new ArrayList<>();
    }
}
