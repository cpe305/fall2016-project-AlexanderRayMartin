package alexanderRayMartin.searchAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Building extends Node implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public String buildingNumber;

    public static ArrayList<Building> buildings;

    private Building(String buildingNumber, String name) {
        this.buildingNumber = buildingNumber;
        this.name = name;
    }

    /** creates a new list if buildings is null */
    public static ArrayList<Building> getBuildings() {
        if (buildings == null) {
            buildings = new ArrayList<Building>();

            FileReader file = Graph.openFile("src/classNumbers.txt");

            loadBuildings(file);
        }

        return buildings;
    }

    /** Converts a file from a FileReader into an array of buildings */
    private static void loadBuildings(FileReader fileReader) {
        String line;
        String items[];
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                items = line.split(",");
                buildings.add(new Building(items[0].trim(), items[1].trim()));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Object o) {
        if (buildingNumber == ((Building) o).buildingNumber)
            return true;

        return false;
    }

    public String toString() {
        return buildingNumber + " " + name;
    }

    public String abreviateString(int length) {
        String temp = name;
        if (name.length() > length) {
            temp = name.substring(0, length - buildingNumber.length());
            temp = temp + "...";
        }
        return buildingNumber + " " + temp;
    }

}