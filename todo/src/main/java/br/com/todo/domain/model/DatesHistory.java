package br.com.todo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class DatesHistory {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime creationDate = LocalDateTime.now();

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime expectedFinalizationDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime realFinalizationDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime stopDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime retakenDate;

    public DatesHistory(LocalDateTime expectedFinalizationDate) {
        this.expectedFinalizationDate = expectedFinalizationDate;
    }

    public DatesHistory() {
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpectedFinalizationDate() {
        return expectedFinalizationDate;
    }

    public void setExpectedFinalizationDate(LocalDateTime expectedFinalizationDate) {
        this.expectedFinalizationDate = expectedFinalizationDate;
    }

    public LocalDateTime getRealFinalizationDate() {
        return realFinalizationDate;
    }

    public void setRealFinalizationDate(LocalDateTime realFinalizationDate) {
        this.realFinalizationDate = realFinalizationDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }

    public LocalDateTime getRetakenDate() {
        return retakenDate;
    }

    public void setRetakenDate(LocalDateTime retakenDate) {
        this.retakenDate = retakenDate;
    }
}
