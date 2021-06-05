package ca.ubc.cs304.model;

public class ReportVehicleModel {
    private String location;
    private String city;
    private int vid;
    private String vtname;
    private String vstatus;

    public ReportVehicleModel(String location, String city, int vid, String vtname, String vstatus) {
        this.location = location;
        this.city = city;
        this.vid = vid;
        this.vtname = vtname;
        this.vstatus = vstatus;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public int getVid() {
        return vid;
    }

    public String getVtname() {
        return vtname;
    }

    public String getVstatus() {
        return vstatus;
    }

    @Override
    public String toString() {
        return vid + "  "  + vstatus + "  " + vtname + "  " + location + "  " + city;

    }
}
