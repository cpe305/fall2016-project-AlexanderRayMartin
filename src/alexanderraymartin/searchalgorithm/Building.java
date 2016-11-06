package alexanderraymartin.searchalgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Building extends Node implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int ABREVIATE_LENGTH = 25;

  public String name;
  public String buildingNumber;

  /**
   * List of buildings in a user's schedule.
   */
  public static ArrayList<Building> buildings;

  private Building(String buildingNumber, String name) {
    this.buildingNumber = buildingNumber;
    this.name = name;
  }

  /**
   * Creates a new list if buildings is null, otherwise loads the list into memory.
   * 
   * @return The list of buildings.
   */
  public static ArrayList<Building> getBuildings() {
    if (buildings == null) {
      buildings = new ArrayList<Building>();

      FileReader file = Graph.openFile("src/classNumbers.txt");

      loadBuildings(file);
    }

    return buildings;
  }

  /**
   * Converts a file from a FileReader into an array of buildings.
   * 
   * @param fileReader The FileReader of a file containing the buildings.
   */
  private static void loadBuildings(FileReader fileReader) {
    String line;
    String[] items;
    BufferedReader bufferedReader;
    try {
      bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        items = line.split(",");
        buildings.add(new Building(items[0].trim(), items[1].trim()));
      }
      bufferedReader.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }


  /**
   * @param object The Building you are checking to be equal.
   * @return The list of buildings.
   */
  public boolean equals(Object object) {
    if (buildingNumber == ((Building) object).buildingNumber) {
      return true;
    }

    return false;
  }

  public String toString() {
    return abbreviateString(ABREVIATE_LENGTH);
  }

  /**
   * @param length The desired length of the string.
   * @return The abbreviated string with '...'.
   */
  public String abbreviateString(int length) {
    String temp = name;
    if (name.length() > length) {
      temp = name.substring(0, length - buildingNumber.length());
      temp = temp + "...";
    }
    return buildingNumber + " " + temp;
  }

}
