package ca.ubc.cs304.model;

import java.time.LocalDate;
import java.util.Date;

public class ReservationModel {
    private final int confNo;
    private final int vid;
    private final String vtname;
    private final String dlicense;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ReservationModel(int confNo, int vid, String vtname, String dlicense, LocalDate fromDate, LocalDate toDate) {
        this.confNo = confNo;
        this.vid = vid;
        this.vtname = vtname;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    public int getConfNo() { return confNo; }

    public String getVtname() { return vtname; }

    public int getVID() { return vid; }

    public LocalDate getFromDate() { return fromDate; }

    public LocalDate getToDate() { return toDate; }

    public String getDlicense() { return dlicense; }
}
