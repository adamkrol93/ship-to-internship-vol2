package model;

import lombok.Getter;

@Getter
public class Cubicle {

    private State state;

    public Cubicle(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "S:" + state.getValue();
    }

    public boolean isFree() {
        return this.state == State.FREE;
    }
}
