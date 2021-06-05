package ca.ubc.cs304.ui.ui_elements;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SuperRentUIClerk extends JFrame {
    private DatabaseConnectionHandler dbch;
    private int rentID = 0;
    private int confNo = 2000;

    // Top bar Elements
    private JPanel panelClerk;
    private JButton logoutButton;
    private JTabbedPane tabbedPane1;

    // Company Rental Report
    private JPanel companyDailyRentalReport;
    private JTextField dateCompanyRentalRep;
    private JButton submitCompanyRentalReport;
    private JTextArea outputTextCompanyRental;

    //Company Return Report
    private JPanel companyDailyReturnReport;
    private JTextField dateCompanyReturnReport;
    private JButton submitCompanyDailyReturnReport;
    private JTextArea companyTextDailyReturn;


    //Branch Daily Return Report
    private JPanel branchDailyReturnReport;
    private JTextField dateBDReturnR;
    private JTextField branchlocationBDReturnRTextField;
    private JTextField branchCityBDReturnRTextField;
    private JButton submitBranchReturnReport;
    private JTextArea outputTextBranchReturn;


    //Branch Daily Rental Report
    private JPanel branchDailyRentalReportPanel;
    private JTextField dateBrRentalReport;
    private JTextField branchLocationBDRentalReport;
    private JTextField branchCityBDRentalReport;
    private JButton submitBranchRentalReport;
    private JTextArea outputTextAreaBranchRental;

    //Make Rental
    private JPanel makeRentalPanel;
    private JTextField ccCardNumMakeRental;
    private JTextField ccCardNameMakeRental;
    private JTextField ccExpDateMakeRental;
    private JTextField resConfNoMakeRental;
    private JTextField brLocationMakeRental;
    private JTextField brCityMakeRental;
    private JLabel dLicenseLabel;
    private JTextField dLicenseMakeRental;
    private JButton submitMakeRental;

    //Make Return
    private JPanel makeReturnPanel;
    private JTextField dateMakeReturn;
    private JTextField odometerMakeReturn;
    private JTextField tankFullMakeReturn;
    private JTextField rentalIDMakeReturn;
    private JButton submitMakeReturn;
    private JTextArea outputTextMakeReturn;
    private JTextArea outputTextReturnWithReservation;


    //Update Customer
    private JPanel updateDeletePanel;
    private JTextField dLicenseUDCust;
    private JTextField addressUDCust;
    private JButton updateCustButton;

    //Delete Customer
    private JTextField dLicenseDeleteCustomer;
    private JButton deleteCustButton;

    private JTextField rentalWOResVTName;






    private JTextArea outputTextRentalWITHOUTReservation;
    private JTextField addressAddNewCust;
    private JTextField dLicenseAddNewCust;
    private JTextField callAddNewCust;
    private JTextField nameAddNewCust;
    private JButton addNewCustomerButton;
    private JButton viewAllCustomers;
    private JTextArea outputViewAll;
    private JTextField rentalWOResVid;
    private JTextField rentalWOResDLicense;
    private JTextField rentalWOResTPstart;
    private JTextField rentalWOResTPend;
    private JTextField ccNameRentalWORes;
    private JTextField ccNUMRentalWORes;
    private JTextField ccExpDateRentalWORes;
    private JButton submitRentalWORes;


    public SuperRentUIClerk(DatabaseConnectionHandler dbch) {
        this.dbch = dbch;

        // COMPANY RENTAL REPORT
        submitCompanyRentalReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateQuery = dateCompanyRentalRep.getText();
                // System.out.println(dateQuery);
                if(checkDateValid2(dateQuery)){
                    CompanyRentalReportModel reports = dbch.generateDailyRentalAllBranches(dateQuery);
                    if(reports.getReportCode() == 1) {
                        JOptionPane.showMessageDialog(null, "ERROR: " + reports.getErrorMessage());
                    } else {
                        outputTextCompanyRental.setText("Requested Rental Report Date: " + dateQuery + "\n\n" + reports);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"ERROR: DATE NOT VALID MUST BE OF FORMAT YYYY-MM-DD and BE A VALID CALENDAR DAY AFTER 1980");
                }

            }
        });

        // BRANCH RENTAL REPORT
        submitBranchRentalReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateQuery = dateBrRentalReport.getText();
                String brLocation = branchLocationBDRentalReport.getText();
                String brCity = branchCityBDRentalReport.getText();

                // System.out.println(dateQuery);
                if(checkDateValid2(dateQuery)){
                    BranchRentalReportModel report = dbch.generateDailyRentalSingleBranch(brLocation, brCity, dateQuery);
                    if(report.getReportCode() == 1) {
                        JOptionPane.showMessageDialog(null, "ERROR: " + report.getErr());
                    } else {
                        outputTextAreaBranchRental.setText("Requested Rental Report Date: " + dateQuery + "\n\n" + report);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"ERROR: DATE NOT VALID MUST BE OF FORMAT YYYY-MM-DD and BE A VALID CALENDAR DAY AFTER 1980");
                }
            }
        });

        // COMPANY RETURN REPORT
        submitCompanyDailyReturnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateQuery = dateCompanyReturnReport.getText();
                if(checkDateValid2(dateQuery)) {
                    CompanyReturnReportModel reports = dbch.generateDailyReturnAllBranches(dateQuery);
                    if(reports.getReportCode() == 1) {
                        JOptionPane.showMessageDialog(null, "ERROR: " + reports.getErrorMessage());
                    } else {
                        companyTextDailyReturn.setText("Requested Return Report Date: " + dateQuery + "\n\n" + reports);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"ERROR: DATE NOT VALID MUST BE OF FORMAT YYYY-MM-DD and BE A VALID CALENDAR DAY AFTER 1980");
                }
            }
        });

        //Branch Daily Return Report
        submitBranchReturnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateQuery = dateBDReturnR.getText();
                String brLocation = branchlocationBDReturnRTextField.getText();
                String brCity = branchCityBDReturnRTextField.getText();

                // System.out.println(dateQuery);
                if(checkDateValid2(dateQuery)){
                    BranchReturnReportModel report = dbch.generateDailyReturnSingleBranch(brLocation, brCity, dateQuery);
                    if(report.getReportCode() == 1) {
                        JOptionPane.showMessageDialog(null, "ERROR: " + report.getErr());
                    } else {
                        outputTextBranchReturn.setText("Requested Return Report Date: " + dateQuery + "\n\n" + report);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: DATE NOT VALID MUST BE OF FORMAT YYYY-MM-DD and BE A VALID CALENDAR DAY AFTER 1980");
                }
            }
        });


        updateCustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dlicense = dLicenseUDCust.getText();
                String newAddress = addressUDCust.getText();

                try {
                    dbch.updateCustomerAddress(dlicense, newAddress);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });
        deleteCustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dlicense = dLicenseDeleteCustomer.getText();

                try {
                    dbch.deleteCustomer(dlicense);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });
        addNewCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dlicense = dLicenseAddNewCust.getText();
                int cell = Integer.parseInt(callAddNewCust.getText());
                String name = nameAddNewCust.getText();
                String address = addressAddNewCust.getText();

                CustomerModel customer = new CustomerModel(dlicense, cell, name, address);
                try {
                    dbch.insertCustomer(customer);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });
        viewAllCustomers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerModel[] customers = null;
                try {
                    customers = dbch.getAllCustomers();
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
                outputViewAll.setText(makeCustomersString(customers));
            }
        });
        submitMakeRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String custDLicense = dLicenseMakeRental.getText();
                CustomerModel customerModel = null;
                try {
                    customerModel = dbch.getCustomer(custDLicense);
                } catch (Exception e1) {
                    // System.out.println(e1.getMessage());
                    JOptionPane.showMessageDialog(null, "ERROR: You are not an existing customer! Please go register.");
                }

                // get Reservation
                int resConfNo = Integer.parseInt(resConfNoMakeRental.getText());
                System.out.println(resConfNo);
                ReservationModel reservationModel = null;
                try {
                    reservationModel = dbch.getReservation(resConfNo);
                } catch (Exception e1) {
                    // System.out.println(e1.getMessage());
                    JOptionPane.showMessageDialog(null, "ERROR: Reservation ConfNo does not exist! Either you have mistyped or you have not yet made a reservation!");
                }

                // get credit card info
                String cardName = ccCardNameMakeRental.getText();
                int cardNo = Integer.parseInt(ccCardNumMakeRental.getText());
                String dateStr = ccExpDateMakeRental.getText();
                // int[] dateParts = new int[0];
                LocalDate expDate = null;

                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter = formatter.withLocale( Locale.CANADA );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
                expDate = LocalDate.parse(dateStr, formatter);

                // Create rental
                rentID++;
                RentalModel model = new RentalModel(rentID, reservationModel.getVID(), reservationModel.getDlicense(),
                        reservationModel.getFromDate(), reservationModel.getToDate(),
                        (float) 0, cardName, cardNo, expDate, resConfNo);
                // insert Rental
                try {
                    dbch.insertRental(model);
                } catch (Exception e1) {
                    if (e1.getMessage().startsWith("[EXCEPTION] ORA-00001: unique constraint")) {
                        JOptionPane.showMessageDialog(null, "ERROR: You have already made this rental.");
                    }
                    // System.out.println(e1.getMessage());
                }
                // Get necessary info to print receipt
                RentalModel modelAdded = null;
                try {
                    modelAdded = dbch.getRentalInfo(rentID);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                    // JOptionPane.showMessageDialog(null, "ERROR: Rental could not be found. Either your rental ID is incorrect (our problem) or the rental has not yet been made (your problem)");
                }
                VehicleModel rentedVehicle = null;
                try {
                    rentedVehicle = dbch.getVehicle(modelAdded.getVid());
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                    // JOptionPane.showMessageDialog(null, "ERROR: Rented Vehicle was not found.");

                }
                // print receipt
                //outputTextReturnWithReservation.setText(displayRentalReceipt(modelAdded, rentedVehicle));
                JOptionPane.showMessageDialog(null, displayRentalReceipt(modelAdded, rentedVehicle));
            }
        });
        submitMakeReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateReturnedString = dateMakeReturn.getText();
                LocalDate dateReturned;
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter = formatter.withLocale( Locale.CANADA );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
                dateReturned = LocalDate.parse(dateReturnedString, formatter);
                float odometer = Float.parseFloat(odometerMakeReturn.getText());
                String tankFull = tankFullMakeReturn.getText().toLowerCase();
                boolean isTankFull = (tankFull.equals("yes"));
                int rid =  Integer.parseInt(rentalIDMakeReturn.getText());

                ReturnModel returnModel = new ReturnModel(rid, dateReturned, odometer, isTankFull, (float) 0);
                try {
                    dbch.insertReturn(returnModel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    if (e1.getMessage().startsWith("[EXCEPTION] ORA-02291: integrity constraint")) {
                        JOptionPane.showMessageDialog(null, "ERROR: You are trying to return a vehicle that has not been rented.");
                    } else if (e1.getMessage().startsWith("[EXCEPTION] ORA-00001: unique constraint")) {
                        JOptionPane.showMessageDialog(null, "ERROR: You have already returned this vehicle.");
                    }
                }
                float value = calculateValue(returnModel);
                try {
                    dbch.updateReturnValue(rid, value);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                returnModel.setValue(value);
                RentalModel rentalModel = null;
                try {
                    rentalModel = dbch.getRentalInfo(returnModel.getRentID());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                VehicleModel vehicleModel = null;
                try {
                    vehicleModel = dbch.getVehicle(rentalModel.getVid());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                // outputTextMakeReturn.setText(displayReturnReceipt(returnModel, rentalModel, vehicleModel));
                JOptionPane.showMessageDialog(null, displayReturnReceipt(returnModel, rentalModel, vehicleModel));
            }
        });
        submitRentalWORes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String vtname = rentalWOResVTName.getText();
                int vid = Integer.parseInt(rentalWOResVid.getText());
                String dlicense = rentalWOResDLicense.getText();
                String fromDateStr = rentalWOResTPstart.getText();
                String toDateStr = rentalWOResTPend.getText();
                LocalDate fromDate;
                LocalDate toDate;
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter = formatter.withLocale( Locale.CANADA );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
                fromDate = LocalDate.parse(fromDateStr, formatter);
                toDate = LocalDate.parse(toDateStr, formatter);
                confNo++;
                ReservationModel reservation = new ReservationModel(confNo, vid, vtname, dlicense, fromDate, toDate);
                try {
                    // reservation.printReservationModel();
                    dbch.insertReservation(reservation);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }

                // get credit card info
                String cardName = ccNameRentalWORes.getText();
                int cardNo = Integer.parseInt(ccNUMRentalWORes.getText());
                String dateStr = ccExpDateRentalWORes.getText();
                LocalDate expDate = null;

                DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter1 = formatter1.withLocale( Locale.CANADA );
                expDate = LocalDate.parse(dateStr, formatter1);

                // Create rental
                rentID++;
                RentalModel model = new RentalModel(rentID, reservation.getVID(), reservation.getDlicense(),
                        reservation.getFromDate(), reservation.getToDate(),
                        (float) 0, cardName, cardNo, expDate, confNo);
                // insert Rental
                try {
                    //model.printRentalModel();
                    dbch.insertRental(model);
                } catch (Exception e1) {
                    if (e1.getMessage().startsWith("[EXCEPTION] ORA-00001: unique constraint")) {
                        JOptionPane.showMessageDialog(null, "ERROR: You have already made this rental.");
                    }
                    System.out.println("HERE1: " + e1.getMessage());
                }
                // Get necessary info to print receipt
                RentalModel modelAdded = null;
                try {
                    modelAdded = dbch.getRentalInfo(rentID);
                } catch (Exception e1) {
                    System.out.println("HERE2: " + e1.getMessage());
                    // JOptionPane.showMessageDialog(null, "ERROR: Rental could not be found. Either your rental ID is incorrect (our problem) or the rental has not yet been made (your problem)");
                }
                VehicleModel rentedVehicle = null;
                try {
                    rentedVehicle = dbch.getVehicle(modelAdded.getVid());
                } catch (Exception e1) {
                    System.out.println("HERE3: " + e1.getMessage());
                    // JOptionPane.showMessageDialog(null, "ERROR: Rented Vehicle was not found.");

                }
                // print receipt
                JOptionPane.showMessageDialog(null, displayRentalReceipt(modelAdded, rentedVehicle));
            }
        });
    }

    private String displayReturnReceipt(ReturnModel returnModel, RentalModel rentalModel, VehicleModel vehicle) {
        String string = "-------RETURN RECEIPT-------\n";
        string +="Rental ID: " + rentalModel.getRid() + "\n";
        string +="Confirmation Number: " + rentalModel.getConfNo() + "\n";
        string += "Dates of Rental: " + rentalModel.getFromDate().toString() + " - " + returnModel.getDate() + "\n";
        string += "Total of Rental: $" + returnModel.getValue() + "\n";
        string += " --- Total was calculated using formula: ---" + "\n";
        string += " timeCost = (WeeksRented * WeeklyRate) + (RemainingDaysRented * DailyRate)" + "\n";
        string += " insuranceCost = (WeeksRented * WeeklyInsuranceRate) + (RemainingDaysRented * DailyInsuranceRate)"+ "\n";
        string += " Total Cost = timeCost + insuranceCost" + "\n";
        string += "Location of Return: " + vehicle.getLocation() + ", " + vehicle.getCity() + "\n";
        string += "-------VEHICLE INFO---------" + "\n";
        string += "Type: " + vehicle.getVtname() + "\n";
        string += "Make: " + vehicle.getMake() + "\n";
        string += "Year: " + vehicle.getYear() + "\n";
        string += "Color: " + vehicle.getColor() + "\n";
        string += "License Plate: " + vehicle.getVlicense() + "\n";
        string += "Odometer: " + vehicle.getOdometer() + "\n";
        string += "-------CUSTOMER INFO---------" + "\n";
        string += "Credit Card Name: " + rentalModel.getCardName() + "\n";
        string += "Customer Driver's License: " + rentalModel.getDlicense() + "\n";
        string += "Credit Card Number: " + rentalModel.getCardNo() + "\n";
        string += "Credit Card Expiration Date: " + rentalModel.getExpDate().toString() + "\n";
        return string;
    }

    private float calculateValue(ReturnModel model) {
        RentalModel rentalModel = null;
        try {
            rentalModel = this.dbch.getRentalInfo(model.getRentID());
            // rentalModel.printRentalModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        VehicleModel vmodel = null;
        try {
            vmodel = this.dbch.getVehicle(rentalModel.getVid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        VehicleTypeModel vTypeModel = null;
        try {
            vTypeModel = this.dbch.getVehicleType(vmodel.getVtname());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDate fromDate = rentalModel.getFromDate();
        LocalDate toDate = model.getDate();

        Period period = Period.between(toDate, fromDate);
        int daysCount = Math.abs(period.getDays());
        int weekCount = Math.abs(daysCount / 7);
        int remainderDays = Math.abs(daysCount % 7);

        float timeValue = weekCount * vTypeModel.getWrate() + remainderDays * vTypeModel.getDrate();

        float insuranceValue = weekCount * vTypeModel.getWirate() + remainderDays * vTypeModel.getDirate();

        return timeValue + insuranceValue;
    }

    private String makeCustomersString(CustomerModel[] customers) {
        String str = "\n";
        for (int i = 0; i < customers.length; i++) {
            str += "\n";
            str += customers[i].toString();
        }
        return str;
    }

    public String displayRentalReceipt(RentalModel rentalModel, VehicleModel vehicle) {
        String string = "-------RENTAL RECEIPT-------\nRental ID: " + rentalModel.getRid() + "\nConfirmation Number: " + rentalModel.getConfNo() + "\n";
        string += "Dates of Rental: " + rentalModel.getFromDate().toString() + " - " + rentalModel.getToDate().toString() + "\n";
        string += "Location of Reservation: " + vehicle.getLocation() + ", " + vehicle.getCity() + "\n";
        string += "-------VEHICLE INFO---------" + "\n";
        string += "Type: " + vehicle.getVtname() + "\n";
        string += "Make: " + vehicle.getMake() + "\n";
        string += "Year: " + vehicle.getYear() + "\n";
        string += "Color: " + vehicle.getColor() + "\n";
        string += "License Plate: " + vehicle.getVlicense() + "\n";
        string += "-------CUSTOMER INFO---------" + "\n";
        string += "Credit Card Name: " + rentalModel.getCardName() + "\n";
        string += "Customer Driver's License: " + rentalModel.getDlicense() + "\n";
        string += "Credit Card Number: " + rentalModel.getCardNo() + "\n";
        string += "Credit Card Expiration Date: " + rentalModel.getExpDate().toString() + "\n\n";
        return string;
    }

    public static boolean checkDateValid2(String date) {
        if(date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            int day, month, year;
            try {
                year = Integer.parseInt(date.substring(0,4));
                month = Integer.parseInt(date.substring(5,7));
                day = Integer.parseInt(date.substring(8,10));
                // System.out.println(day + " "+  month + " " + year);
            } catch (NumberFormatException e) {
                return false;
            }
            if(month % 2 == 1) {
                return 1 <= day && day <= 30 && 1 <= month && month <= 12 && 1980 <= year && year <= 2019;
            } else if(month == 2){
                return 1 <= day && day <= 28 && 1 <= month && month <= 12 && 1980 <= year && year <= 2019;
            } else {
                return 1 <= day && day <= 31 && 1 <= month && month <= 12 && 1980 <= year && year <= 2019;
            }
        } else {
            return false;
        }
    }

    public static int[] extractDate2(String date) {
        int[] dateNumbers = new int[3];
        try {
            dateNumbers[0] = Integer.parseInt(date.substring(0,1));
            dateNumbers[1] = Integer.parseInt(date.substring(3,4));
            dateNumbers[2] = Integer.parseInt(date.substring(6,9));
            return dateNumbers;
        } catch (NumberFormatException e) {
            return new int[1];
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SuperRentUI");
        DatabaseConnectionHandler database = new DatabaseConnectionHandler();
        database.login("ora_mderose", "a88080114");
        // System.out.println(SuperRentUIClerk.checkDateValid("1990-02-01"));
        frame.setContentPane(new SuperRentUIClerk(database).panelClerk);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
