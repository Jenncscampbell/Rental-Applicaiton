package ca.ubc.cs304.model;

//create table returns (
//        return_rentID integer not null PRIMARY KEY,
//        return_date date,
//        return_time time,
//        return_odometer float,
//        return_fulltank boolean,
//        return_value float,
//        foreign key (return_rentID) references rental
//        );

import java.time.LocalDate;
import java.util.Date;

public class ReturnModel {
    private final int rentID;
    private final LocalDate date; // return date
    private final float odometer;
    private final boolean fullTank;
    private Float value; // cost

    public ReturnModel(int rentID, LocalDate date, float odometer, boolean fullTank, Float value) {
        this.rentID = rentID;
        this.date = date;
        this.odometer = odometer;
        this.fullTank = fullTank;
        this.value = value;
    }

    public int getRentID() { return rentID; }

    public LocalDate getDate() { return date; }

    public float getOdometer() { return odometer; }

    public boolean getFullTank() { return fullTank; }

    public float getValue() { return value; }

    public void setValue(float val) {
        this.value = val;
    }
}
