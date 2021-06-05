package ca.ubc.cs304.model;

import java.util.ArrayList;

public class BranchReturnReportModel {
    private final int reportCode;
    private final String err;
    private final String location;
    private final String city;
    private final int SUVCount;
    private final float SUVsubtotal;
    private final int TruckCount;
    private final float TruckSubtotal;
    private final int EcononyCount;
    private final float EconomySubtotal;
    private final int CompactCount;
    private final float CompactSubtotal;
    private final int MidsizeCount;
    private final float MidsizeSubtotal;
    private final int StandardCount;
    private final float StandardSubtotal;
    private final int FullsizeCount;
    private final float FullsizeSubtotal;
    private final int BranchVehicleCount;
    private final float BranchRevenueSubtotal;
    private final ArrayList<ReportVehicleModel> reports;

    public BranchReturnReportModel(ArrayList<ReportVehicleModel> reports, int reportCode, String location, String city, int SUVCount, float SUVsubtotal,
                                   int TruckCount, float Trucksubtotal, int EconomyCount, float EconomySubtotal,
                                   int CompactCount, float CompactSubtotal, int MidsizeCount, float MidsizeSubtotal,
                                   int StandardCount, float StandardSubtotal, int FullsizeCount, float FullsizeSubtotal,
                                   int BranchVehicleCount, float BranchRevenueSubtotal) {
        this.reportCode = reportCode;
        this.reports = reports;
        this.err = "";
        this.location = location;
        this.city = city;
        this.SUVCount = SUVCount;
        this.SUVsubtotal = SUVsubtotal;
        this.TruckCount = TruckCount;
        this.TruckSubtotal = Trucksubtotal;
        this.EcononyCount = EconomyCount;
        this.EconomySubtotal = EconomySubtotal;
        this.CompactCount = CompactCount;
        this.CompactSubtotal = CompactSubtotal;
        this.MidsizeCount = MidsizeCount;
        this.MidsizeSubtotal = MidsizeSubtotal;
        this.StandardCount = StandardCount;
        this.StandardSubtotal = StandardSubtotal;
        this.FullsizeCount = FullsizeCount;
        this.FullsizeSubtotal = FullsizeSubtotal;
        this.BranchVehicleCount = BranchVehicleCount;
        this.BranchRevenueSubtotal = BranchRevenueSubtotal;
    }

    public BranchReturnReportModel(int reportCode, String err) {
        this.reportCode = reportCode;
        this.reports = null;
        this.err = err;
        this.location = "";
        this.city = "";
        this.SUVCount = 0;
        this.SUVsubtotal = 0;
        this.TruckCount = 0;
        this.TruckSubtotal = 0;
        this.EcononyCount = 0;
        this.EconomySubtotal = 0;
        this.CompactCount = 0;
        this.CompactSubtotal = 0;
        this.MidsizeCount = 0;
        this.MidsizeSubtotal = 0;
        this.StandardCount = 0;
        this.StandardSubtotal = 0;
        this.FullsizeCount = 0;
        this.FullsizeSubtotal = 0;
        this.BranchVehicleCount = 0;
        this.BranchRevenueSubtotal = 0;
    }

    public int getReportCode() { return reportCode; }

    public String getLocation() { return location; }

    public String getCity() { return city; }

    public int getSUVCount() { return SUVCount; }

    public float getSUVsubtotal() { return SUVsubtotal; }

    public int getTruckCount() { return TruckCount; }

    public float getTruckSubtotal() { return TruckSubtotal; }

    public int getEcononyCount() { return EcononyCount; }

    public float getEconomySubtotal() { return EconomySubtotal; }

    public int getCompactCount() { return CompactCount; }

    public String getErr() { return err; }

    public float getCompactSubtotal() { return CompactSubtotal; }

    public int getMidsizeCount() { return MidsizeCount; }

    public float getMidsizeSubtotal() { return MidsizeSubtotal; }

    public int getStandardCount() { return StandardCount; }

    public float getStandardSubtotal() { return StandardSubtotal; }

    public int getFullsizeCount() { return FullsizeCount; }

    public float getFullsizeSubtotal() { return FullsizeSubtotal; }

    public int getBranchVehicleCount() { return BranchVehicleCount; }

    public float getBranchRevenueSubtotal() { return BranchRevenueSubtotal; }

    @Override
    public String toString() {
        String string = "";
        string += "Location: " + location + "\n";
        string += "City: " + city + "\n";
        string += "SUV Count: " + SUVCount + "\n";
        string += "SUV Subtotal: " + "$" + SUVsubtotal + "\n";
        string += "Truck Count: " + TruckCount + "\n";
        string += "Truck Subtotal: " + "$" + TruckSubtotal + "\n";
        string += "Economy Count: " + EcononyCount + "\n";
        string += "Economy Subtotal: " + "$" + EconomySubtotal + "\n";
        string += "Compact Count: " + CompactCount + "\n";
        string += "Compact Subtotal: " + "$" + CompactSubtotal + "\n";
        string += "Mid-size Count: " + MidsizeCount + "\n";
        string += "Mid-size Subtotal: " + "$" +MidsizeSubtotal + "\n";
        string += "Standard Count: " + StandardCount + "\n";
        string += "Standard Subtotal: " + "$" +StandardSubtotal + "\n";
        string += "Full-size Count: " + FullsizeCount + "\n";
        string += "Full-size Subtotal: " + "$" + FullsizeSubtotal + "\n";
        string += "Total Vehicle Returns: " + BranchVehicleCount + "\n";
        string += "Total Return Revenue: " + BranchRevenueSubtotal + "\n\n\n";
        for(int i = 0; i<reports.size(); i++) {
            if(reports.get(i).getLocation().equals(location) && reports.get(i).getCity().equals(city)) {
                string += reports.get(i).toString();
            }
        }
        return string;
    }
}
