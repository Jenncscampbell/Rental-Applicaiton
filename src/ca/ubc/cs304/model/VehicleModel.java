package ca.ubc.cs304.model;

public class VehicleModel {

    private final int vid;
    private final String vlicense;
    private final String make;
    private final int year;
    private final String color;
    private final float odometer;
    private final String vstatus;
    private final String vtname;
    private final String location;
    private final String city;

    public VehicleModel(int vid, String vlicense, String make, int year, String color,
                        float odometer, String vstatus, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.vstatus = vstatus;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public int getVid() { return vid; }

    public String getVlicense() { return vlicense; }

    public String getMake() { return make; }

    public int getYear() { return year; }

    public String getColor() { return color; }

    public float getOdometer() { return odometer; }

    public String getVstatus() { return vstatus; }

    public String getVtname() { return vtname; }

    public String getLocation() { return location; }

    public String getCity() { return city; }


}
