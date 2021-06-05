package ca.ubc.cs304.model;

import java.time.LocalDate;
import java.util.Date;

public class RentalModel {

//    foreign key (cellphone) references customer,
//    foreign key (vid) references forrent,
//    foreign key (confNo) references reservations,
//

    private final int rid;
    private final int vid; // vehicle ID
    private final String dlicense;
    private final LocalDate fromDate; // includes fromTime
    // private final Time fromTime;
    private final LocalDate toDate; // includes toTime
    // private final Time toTime;
    private final Float odometer;
    private final String cardName;
    private final int cardNo;
    private final LocalDate expDate;
    private final int confNo;

    public RentalModel(int rid, int vid, String dlicense, LocalDate fromDate, LocalDate toDate,
                       Float odometer, String cardName, int cardNo, LocalDate expDate, int confNo) {
        this.rid = rid;
        this.vid = vid;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public int getRid() { return rid; }

    public int getVid() { return vid; }

    public String getDlicense() { return dlicense; }

    public LocalDate getFromDate() { return fromDate; }

    public LocalDate getToDate() { return toDate; }

    public Float getOdometer() { return odometer; }

    public String getCardName() { return cardName; }

    public int getCardNo() { return cardNo; }

    public LocalDate getExpDate() { return expDate; }

    public int getConfNo() { return confNo; }

}
