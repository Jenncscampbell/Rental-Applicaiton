package ca.ubc.cs304.model;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {
    private final int vid;
    private final String make;
    private final String name;
    private final int year;
    private final String color;
    private final String amt;


    public SearchModel(int vid, String make, String name, int year, String color, String amt) {
        this.vid = vid;
        this.make = make;
        this.name = name;
        this.year = year;
        this.color = color;
        this.amt = amt;
    }

    public static String toStringSearch(ArrayList<SearchModel> result) {
        String resultStr = " ";
        for (int i = 0; i< result.size(); i++) {
            int vid = result.get(i).getVid();
            String make = result.get(i).getMake();
            String name = result.get(i).getName();
            int yr = result.get(i).getYear();
            String color = result.get(i).getColor();
            resultStr += vid +  " " + make +  " " + name +  " " + yr + " " +  color + "\n";
        }
        return resultStr;
    }

    public int getVid() {
        return vid;
    }

    public String getMake() {
        return make;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getAmt() {
        return amt;
    }


}
