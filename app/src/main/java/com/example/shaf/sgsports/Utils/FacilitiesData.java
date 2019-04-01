package com.example.shaf.sgsports.Utils;

import android.util.Log;

import com.example.shaf.sgsports.Model.Facility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FacilitiesData {
    private static final String COMMA_DELIMITER = ",";
    private static final String UNDERSCORE = "_";
    private static ArrayList<Facility> facilList = new ArrayList<>();

    FacilitiesData() {

    }

    public static ArrayList<Facility> run(InputStream i) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(i, "UTF-8"));
            Integer counter = 1000;
            String line = br.readLine();
            while(line != null) {
                String[] columns;
                columns = line.split(COMMA_DELIMITER);
                for (int j=0; j<6; j++) {
                    columns[j] = columns[j].replace(UNDERSCORE, ",");
                }
                Facility temp = new Facility(counter.toString() ,columns[0], columns[1], columns[2], columns[4], columns[5], columns[3]);
                counter++;
                facilList.add(temp);
//                System.out.println(line);
                line = br.readLine();
                line = br.readLine();
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Log.e("FACILITIES DATA: ", facilList.toString());

        return facilList;
    }
}