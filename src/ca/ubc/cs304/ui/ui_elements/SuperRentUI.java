package ca.ubc.cs304.ui.ui_elements;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.CustomerModel;
import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.model.SearchModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class SuperRentUI extends JFrame {
    private JPanel mainPanel;
    private JButton loginToClerkButton;
    private JTabbedPane tabbedPane1;
    // Check Availability Text Fields
    private JPanel panel1;
    private JTextField branchLocationCA;
    private JTextField timePeriodFromDateCA;
    private JTextField timePeriodToDateCA;
    private JTextField branchCityCA;
    private JTextField vehicleTypeCA;
    private JButton submitbuttonCA;
    private JTextField AmtAvailable;
    private JLabel AmtAvail;

    // Make Reservation
    private JPanel panel2;
    private JTextField resVid;
    private JTextField dlicense;
    private JTextField vehicleTypeR;
    private JTextField timePeriodStartR;
    private JTextField timePeriodEndR;
    private JButton submitButtonR;
    private int confNo = 22300;

    // Add Customer
    private JTextField custNameAddCust;
    private JTextField custLisenceAddCust;
    private JTextField custCellphone;
    private JTextField custAddressAddCust;
    private JButton submitButtonAddCust;
    private JPanel panel3;

    private JTextArea ListAvailableCars;
    private JLabel r;
    private JTextField newVid;
    private JTextField newCell;
    private JTextField newName;
    private JTextField newAdd;
    private JTextField newdl;
    private JTextField newStartDate;
    private JTextField NewEndDate;
    private JTextField newVType;
    private JButton submitButtonNEW;


    private DatabaseConnectionHandler dbch;

    public SuperRentUI(DatabaseConnectionHandler db) {

        submitbuttonCA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date ldatefrom = null;
                if (timePeriodFromDateCA.getText().isEmpty()) {
                    ldatefrom = null;
                } else {
                    String datestr = timePeriodFromDateCA.getText();
                    //System.out.println("Raw date input: " + datestr);
                    if (checkDateValid(datestr)) {
                        ldatefrom = extractDate(datestr);
                        //System.out.println("ldateSuper: " + ldatefrom.toString());
                    } else {
                        System.out.println("Date from is incorrect.");
                    }

                }
                String dateend;
                Date ldateTo = null;
                if (timePeriodToDateCA.getText().isEmpty()) {
                    ldateTo = null;
                } else {
                    dateend = timePeriodToDateCA.getText();
                    //System.out.println("Raw date input: " + dateend);
                    if (checkDateValid(dateend)) {
                        ldateTo = extractDate(dateend);
                        //System.out.println("ldateSuper: " + ldateTo.toString());
                    } else {
                        System.out.println("Date to is incorrect.");
                    }
                }


                String branchloc;
                String branchcity;
                String vtype;
                //System.out.println("Going to the SQL");


                if (branchLocationCA.getText().isEmpty()) {
                    branchloc = null;
                } else {
                    branchloc = branchLocationCA.getText();
                }
                if (branchCityCA.getText().isEmpty()) {
                    branchcity = null;
                } else {
                    branchcity = branchCityCA.getText();
                }

                if (vehicleTypeCA.getText().isEmpty()) {
                    vtype = null;
                } else {
                    vtype = vehicleTypeCA.getText();
                }

                if ((ldatefrom == null && ldateTo != null) || (ldatefrom != null && ldateTo == null)) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid date criteria. Date will be ignored.");
                }
                System.out.println("UI Dates: " + ldatefrom + " " + ldateTo);
                String result = db.veiwAmtVehiclesAvailable(branchloc, branchcity,
                        ldatefrom, ldateTo, vtype);
                System.out.println(result);
                AmtAvailable.setText(result); //put to UI
                if (result == "0") {
                    JOptionPane.showMessageDialog(null,
                            "There are no vehicles available matching your search criteria. Plase try again.");
                }
            }
        });

        //Works!!
        submitButtonAddCust.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dlic = custLisenceAddCust.getText();
                int cell = Integer.parseInt(custCellphone.getText());

                String name = custNameAddCust.getText();
                String add = custAddressAddCust.getText();

                CustomerModel model = new CustomerModel(dlic, cell, name, add);

                try {
                    db.insertCustomer(model);
                    JOptionPane.showMessageDialog(null, "Customer Successfully Added");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Customer could not be added");

                }


            }
        });
        //after you have found search results:
        AmtAvailable.addKeyListener(new KeyAdapter() {
        });
        AmtAvailable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Date ldatefrom = null;
                if (timePeriodFromDateCA.getText().isEmpty()) {
                    ldatefrom = null;
                } else {
                    String datestr = timePeriodFromDateCA.getText();
                    //System.out.println("Raw date input: " + datestr);
                    if (checkDateValid(datestr)) {
                        ldatefrom = extractDate(datestr);
                        //System.out.println("ldateSuper: " + ldatefrom.toString());
                    } else {
                        System.out.println("Date from is incorrect.");
                    }

                }
                String dateend;
                Date ldateTo = null;
                if (timePeriodToDateCA.getText().isEmpty()) {
                    ldateTo = null;
                } else {
                    dateend = timePeriodToDateCA.getText();
                    //System.out.println("Raw date input: " + dateend);
                    if (checkDateValid(dateend)) {
                        ldateTo = extractDate(dateend);
                        //System.out.println("ldateSuper: " + ldateTo.toString());
                    } else {
                        System.out.println("Date to is incorrect.");
                    }
                }


                String branchloc;
                String branchcity;
                String vtype;
                //System.out.println("Going to the SQL");


                if (branchLocationCA.getText().isEmpty()) {
                    branchloc = null;
                } else {
                    branchloc = branchLocationCA.getText();
                }
                if (branchCityCA.getText().isEmpty()) {
                    branchcity = null;
                } else {
                    branchcity = branchCityCA.getText();
                }

                if (vehicleTypeCA.getText().isEmpty()) {
                    vtype = null;
                } else {
                    vtype = vehicleTypeCA.getText();
                }

                ArrayList<SearchModel> result = new ArrayList<>();
                result = db.veiwVehiclesAvailable(branchloc, branchcity,
                        ldatefrom, ldateTo, vtype);
                String availableCars = SearchModel.toStringSearch(result);
                ListAvailableCars.setText(availableCars);



            }

        });


        submitButtonR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confNo++;
                String vidStr = resVid.getText();
                int vid = Integer.parseInt(vidStr);
                String vtname = vehicleTypeR.getText();
                String dlicStr = dlicense.getText();

                String fromDateStr = timePeriodStartR.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter = formatter.withLocale(Locale.CANADA);
                LocalDate dateStart = LocalDate.parse(fromDateStr, formatter);


                String toDateStr =  timePeriodEndR.getText();
                DateTimeFormatter formatterTo = DateTimeFormatter.ISO_LOCAL_DATE;
                formatterTo = formatterTo.withLocale(Locale.CANADA);
                LocalDate toDate = LocalDate.parse(toDateStr, formatterTo);

                ReservationModel model = new ReservationModel(confNo, vid, vtname, dlicStr , dateStart, toDate);
                try {
                    db.insertReservation(model);
                    JOptionPane.showMessageDialog(null, "Reservation added.");
                    JOptionPane.showMessageDialog(null, "Confirmation: " + confNo + "\n" +
                            "Vehicle ID: " + vid + "\n" +
                            "Vehicle Type: " + vtname + "\n" +
                            "Start Date: " + dateStart + "\n" +
                            "End Date: " + toDate + "\n");


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not make your requested reservation.");

                }


            }
        });


        submitButtonNEW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confNo++;
                String vidStr = newVid.getText();
                int vid = Integer.parseInt(vidStr);
                int cell = Integer.parseInt(newCell.getText());
                String name = newName.getText();
                String adr = newAdd.getText();
                String dl = newdl.getText();
                String VType =  newVType.getText();

                String fromDateStr = newStartDate.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                formatter = formatter.withLocale(Locale.CANADA);
                LocalDate dateStart = LocalDate.parse(fromDateStr, formatter);

                String toDateStr =  NewEndDate.getText();
                DateTimeFormatter formatterTo = DateTimeFormatter.ISO_LOCAL_DATE;
                formatterTo = formatterTo.withLocale(Locale.CANADA);
                LocalDate toDate = LocalDate.parse(toDateStr, formatterTo);


                CustomerModel model = new CustomerModel(dl, cell, name, adr);

                try {
                    db.insertCustomer(model);
                    JOptionPane.showMessageDialog(null, "Customer Successfully Added");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Customer could not be added");
                }



                ReservationModel modelR = new ReservationModel(confNo, vid, VType, dl , dateStart, toDate);
                try {
                    db.insertReservation(modelR);
                    JOptionPane.showMessageDialog(null, "Reservation added.");
                    JOptionPane.showMessageDialog(null, "Confirmation: " +confNo + "\n" +
                            "Vehicle ID: " + vid + "\n" +
                            "Vehicle Type: " + VType + "\n" +
                            "Start Date: " + dateStart + "\n" +
                            "End Date: " + toDate + "\n");


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not make your requested reservation.");

                }

            }
        });
    }


    public static boolean checkDateValid(String date) {
        if(date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            int day, month, year;
            try {
                day = Integer.parseInt(date.substring(0,2));
                month = Integer.parseInt(date.substring(3,5));
                year = Integer.parseInt(date.substring(6,10));
                //System.out.println("checkDatePrint" + day + " "+  month + " " + year);
            } catch (NumberFormatException e) {
                return false;
            }
            if(month % 2 == 1) {
                return 1 <= day && day <= 30 && 1 <= month && month <= 12 && 1980 <= year && year <= 2020;
            } else if(month == 2){
                return 1 <= day && day <= 28 && 1 <= month && month <= 12 && 1980 <= year && year <= 2020;
            } else {
                return 1 <= day && day <= 31 && 1 <= month && month <= 12 && 1980 <= year && year <= 2020;
            }
        } else {
            return false;
        }
    }

    public static Date extractDate(String date) {
        Date localD;
        try {
            int day, month, year;
            day = Integer.parseInt(date.substring(0,2));
            month = Integer.parseInt(date.substring(3,5));
            year = Integer.parseInt(date.substring(6,10));
            LocalDate localDate = LocalDate.of(year,month,day);
            localD = Date.valueOf(localDate);
            //System.out.println("extracted date: " + localD);


            return localD;
        } catch (NumberFormatException e) {
            //System.out.println("Something went wrong in extract Date");
        }
        return null;
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame("SuperRentUI");
        try{
            DatabaseConnectionHandler database = new DatabaseConnectionHandler();
            database.login("ora_jencam88", "a81998122");
            frame.setContentPane(new SuperRentUI(database).mainPanel);
        } catch (Exception e) {

        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



}
