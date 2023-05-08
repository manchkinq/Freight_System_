package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Destination {
    private int id;
    private String startCity;
    private long startLn;
    private long startLat;
    private String endCity;
    private long endLn;
    private long endLat;
    private LocalDate dateCreated;
    private int truck_id;
    private int cargo_id;
    private int responsibleManager_id;
    private int driver_id;



    public Destination(String startCity, long startLn, long startLat, String endCity, long endLn, long endLat, int truck_id, int cargo_id, int responsibleManager_id) {
        this.startCity = startCity;
        this.startLn = startLn;
        this.startLat = startLat;
        this.endCity = endCity;
        this.endLn = endLn;
        this.endLat = endLat;
        this.dateCreated = LocalDate.now();
        this.truck_id = truck_id;
        this.cargo_id = cargo_id;
        this.responsibleManager_id = responsibleManager_id;
    }






    public String toString() {
        return "StartCity: " + startCity + " || StartLn: " + startLn + " || StartLat: " + startLat
                + "\nEndCity: " + endCity + " || EndLn: " + endLn + " || EndLat: " + endLat
                + "\nDate created: " + dateCreated + "|| Driver id: " + driver_id;


    }


}
