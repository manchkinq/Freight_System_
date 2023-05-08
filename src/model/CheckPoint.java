package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CheckPoint {
    private int id;
    private String title;
    private boolean longStop;
    private LocalDate chptDateArrived;



    public CheckPoint(String title, boolean longStop, LocalDate chptDateArrived) {
        this.title = title;
        this.longStop = longStop;
        this.chptDateArrived = chptDateArrived;
    }



    @Override
    public String toString() {
        if (longStop) {
            return "Title: " + title + " || Long stop || Date arrived : " + chptDateArrived;
        } else {
            return "Title: " + title + " || Short stop || Date arrived : " + chptDateArrived;
        }
    }
}
