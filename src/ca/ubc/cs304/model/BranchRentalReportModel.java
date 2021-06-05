package ca.ubc.cs304.model;

import java.util.ArrayList;

public class BranchRentalReportModel {
    private final int reportCode;
    private final String err;
    private final String location;
    private final String city;
    private final int SUVCount;
    private final int TruckCount;
    private final int EcononyCount;
    private final int CompactCount;
    private final int MidsizeCount;
    private final int StandardCount;
    private final int FullsizeCount;
    private final int Totalcount;
    private final ArrayList<ReportVehicleModel> reports;

    public BranchRentalReportModel(ArrayList<ReportVehicleModel> reports, int reportCode, String location, String city, int SUVCount, int TruckCount, int EconomyCount, int CompactCount, int MidsizeCount, int StandardCount, int FullsizeCount, int TotalCount) {
        this.reports = reports;
        this.reportCode = reportCode;
        this.err = "";
        this.location = location;
        this.city = city;
        this.SUVCount = SUVCount;
        this.TruckCount = TruckCount;
        this.EcononyCount = EconomyCount;
        this.CompactCount = CompactCount;
        this.MidsizeCount = MidsizeCount;
        this.StandardCount = StandardCount;
        this.FullsizeCount = FullsizeCount;
        this.Totalcount = TotalCount;
    }

    public BranchRentalReportModel(int reportCode, String err) {
        this.reportCode = reportCode;
        this.reports = null;
        this.err = err;
        this.location = "";
        this.city = "";
        this.SUVCount = 0;
        this.TruckCount = 0;
        this.EcononyCount = 0;
        this.CompactCount = 0;
        this.MidsizeCount = 0;
        this.StandardCount = 0;
        this.FullsizeCount = 0;
        this.Totalcount = 0;
    }

    public ArrayList<ReportVehicleModel> getReports() { return reports; }

    public String getErr() { return err; }

    public int getReportCode() { return reportCode; }

    public String getLocation() { return location; }

    public String getCity() { return city; }

    public int getSUVCount() { return SUVCount; }

    public int getTruckCount() { return TruckCount; }

    public int getEcononyCount() { return EcononyCount; }

    public int getCompactCount() { return CompactCount; }

    public int getMidsizeCount() { return MidsizeCount; }

    public int getStandardCount() { return StandardCount; }

    public int getFullsizeCount() { return FullsizeCount; }

    public int getTotalcount() { return Totalcount; }

    @Override
    public String toString(){
        String string = "";
        string += "Location: " + this.location + "\n";
        string += "City: " + this.city + "\n";
        string += "SUV Count: " + this.SUVCount + "\n";
        string += "Truck Count: " + this.TruckCount + "\n";
        string += "Economy Count: " + this.EcononyCount + "\n";
        string += "Compact Count: " + this.CompactCount + "\n";
        string += "Midsize Count: " + this.MidsizeCount + "\n";
        string += "Standard Count: " + this.StandardCount +"\n";
        string += "Fullsize Count: " + this.FullsizeCount + "\n";
        string += "Total Vehicle Count: " + this.Totalcount + "\n";
        string += "LIST OF RENTED VEHICLES AT BRANCH\n";
        //System.out.println(reports.size());
        for(int i = 0; i<reports.size(); i++) {
            // System.out.println(reports.get(i).toString());
            if(reports.get(i).getLocation().equals(location) && reports.get(i).getCity().equals(city)) {
                string += reports.get(i).toString() + "\n";
            }
        }
        return string;
    }
}
