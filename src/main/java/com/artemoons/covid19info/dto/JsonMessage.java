package com.artemoons.covid19info.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class JsonMessage {

    private Long infectedOverall;

    private Long healedOverall;

    private Long deathsOverall;

    private Long activeOverall;

    public JsonMessage(final Long infectedOverall,
                       final Long healedOverall,
                       final Long deathsOverall,
                       final Long activeOverall) {
        this.infectedOverall = infectedOverall;
        this.healedOverall = healedOverall;
        this.deathsOverall = deathsOverall;
        this.activeOverall = activeOverall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonMessage that = (JsonMessage) o;
        return Objects.equals(infectedOverall, that.infectedOverall) &&
                        Objects.equals(healedOverall, that.healedOverall) &&
                        Objects.equals(deathsOverall, that.deathsOverall) &&
                        Objects.equals(activeOverall, that.activeOverall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infectedOverall, healedOverall, deathsOverall, activeOverall);
    }
}
