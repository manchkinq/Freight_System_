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

public class Cargo {
    private int id;
    private String title;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    private double weight;
    private CargoType cargoType;
    private String description;
    private String customer;

    public Cargo(String title, double weight, CargoType cargoType, String description, String customer) {
        this.title = title;
        this.weight = weight;
        this.cargoType = cargoType;
        this.description = description;
        this.customer = customer;
        this.dateCreated = LocalDate.now();
    }

    public Cargo(int id, String title, LocalDate dateCreated, double weight, CargoType cargoType, String description, String customer) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.weight = weight;
        this.cargoType = cargoType;
        this.description = description;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Title: " + title + " || Weight: " + weight + " || CargoType: " + cargoType + "\nDescription: " + description + " || Customer: " + customer + " || Date: " + dateCreated;
    }
}
