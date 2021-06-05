package ca.ubc.cs304.model;

import java.util.List;

public class CompanyReturnReportModel {
    private final int reportCode;
    private final String err;
    private final List<BranchReturnReportModel> branchReturns;
    private final int branchReturnVehicleCount;
    private final float branchRevenueTotal;

    public CompanyReturnReportModel(int reportCode, String err, List<BranchReturnReportModel> branchReturns,
                                    int branchReturnVehicleCount, float branchRevenueTotal) {
        this.reportCode = reportCode;
        this.err = err;
        this.branchReturns = branchReturns;
        this.branchReturnVehicleCount = branchReturnVehicleCount;
        this.branchRevenueTotal = branchRevenueTotal;
    }

    public String getErrorMessage() { return err; }

    public int getReportCode() { return reportCode; }

    public List<BranchReturnReportModel> getBranchReturns() { return branchReturns; }

    public int getBranchReturnVehicleCount() { return branchReturnVehicleCount; }

    public float getBranchRevenueTotal() { return branchRevenueTotal; }

    @Override
    public String toString() {
        String string = "COMPANY RETURN REPORT \n\n";
        for(int i = 0; i< branchReturns.size(); i++) {
            string += branchReturns.get(i).toString();
        }
        string += "NEW RETURNED VEHICLE COUNT: " + branchReturnVehicleCount + "\n";
        string += "REVENUE TOTAL: " + branchRevenueTotal;
        return string;

    }
}
