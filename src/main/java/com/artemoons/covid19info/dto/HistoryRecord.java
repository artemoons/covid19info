package com.artemoons.covid19info.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class HistoryRecord {

    private String recordId;

    private String date;

    private Long infectedOverall;

    private Long healedOverall;

    private Long deathsOverall;

    private Long activeOverall;

    public HistoryRecord(final String recordId,
                         final String date,
                         final Long infectedOverall,
                         final Long healedOverall,
                         final Long deathsOverall,
                         final Long activeOverall) {
        this.recordId = recordId;
        this.date = date;
        this.infectedOverall = infectedOverall;
        this.healedOverall = healedOverall;
        this.deathsOverall = deathsOverall;
        this.activeOverall = activeOverall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryRecord that = (HistoryRecord) o;
        return Objects.equals(recordId, that.recordId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(infectedOverall, that.infectedOverall) &&
                Objects.equals(healedOverall, that.healedOverall) &&
                Objects.equals(deathsOverall, that.deathsOverall) &&
                Objects.equals(activeOverall, that.activeOverall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, date, infectedOverall, healedOverall, deathsOverall, activeOverall);
    }
}
