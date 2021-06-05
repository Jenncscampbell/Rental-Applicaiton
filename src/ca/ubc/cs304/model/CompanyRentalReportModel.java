package ca.ubc.cs304.model;

import java.util.List;

public class CompanyRentalReportModel {
    private final int reportCode;
    private final String err;
    private final List<BranchRentalReportModel> branchReports;
    private final int totalRentedVehicles;


    public CompanyRentalReportModel(int reportCode, String err, List<BranchRentalReportModel> branchReports,
                                    int totalRentedVehicles) {
        this.reportCode = reportCode;
        this.err = err;
        this.branchReports = branchReports;
        this.totalRentedVehicles = totalRentedVehicles;
    }

    public int getReportCode() { return reportCode; }

    public String getErrorMessage() { return err; }

    public List<BranchRentalReportModel> getBranchReports() { return branchReports; }

    public int getTotalRentedVehicles() { return totalRentedVehicles; }

    @Override
    public String toString() {
        String string = "COMPANY RENTAL REPORT\n\n";
        for(int i = 0; i< branchReports.size(); i++) {
            string += branchReports.get(i).toString() + "\n";
        }
        string += "NEW VEHICLE RENTAL COUNT: " + totalRentedVehicles;
        return string;
    }
}
