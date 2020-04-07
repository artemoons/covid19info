package com.artemoons.covid19info.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class HistoryRecord {

    private String recordId;

    private String date;

    private Long testsOverall;

    private Long infectedOverall;

    private Long infectedLastDay;

    private Long healedOverall;

    private Long deathsOverall;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryRecord that = (HistoryRecord) o;
        return Objects.equals(recordId, that.recordId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(testsOverall, that.testsOverall) &&
                Objects.equals(infectedOverall, that.infectedOverall) &&
                Objects.equals(infectedLastDay, that.infectedLastDay) &&
                Objects.equals(healedOverall, that.healedOverall) &&
                Objects.equals(deathsOverall, that.deathsOverall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, date, testsOverall, infectedOverall, infectedLastDay, healedOverall, deathsOverall);
    }
}
