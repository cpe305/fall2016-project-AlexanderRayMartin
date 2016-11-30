package alexanderraymartin.searchalgorithm;

import alexanderraymartin.main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Alex Martin.
 *
 */
public class Building implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int ABREVIATE_LENGTH = 25;

  /**
   * The building name.
   */
  private String name;
  /**
   * The building number.
   */
  private String buildingNumber;
  /**
   * The node number.
   */
  private int nodeNumber;

  /**
   * List of buildings in a user's schedule.
   */
  private static ArrayList<Building> buildings;

  private Building(String buildingNumber, String name, int nodeNumber) {
    this.buildingNumber = buildingNumber;
    this.name = name;
    this.nodeNumber = nodeNumber;
  }


  /**
   * @return The building name.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The building number.
   */
  public String getBuildingNumber() {
    return buildingNumber;
  }

  /**
   * @return The node number.
   */
  public int getNodeNumber() {
    return nodeNumber;
  }

  /**
   * Creates a new list if buildings is null, otherwise loads the list into memory.
   * 
   * @return The list of buildings.
   */
  public static ArrayList<Building> getBuildings() {
    if (buildings == null) {
      buildings = new ArrayList<Building>();

      BufferedReader bufferedReader = Graph.openFile("classNumbers.txt");

      loadBuildings(bufferedReader);
    }

    return buildings;
  }

  /**
   * Converts a file from a BufferedReader into an array of buildings.
   * 
   * @param bufferedReader The BufferedReader of a file containing the buildings.
   */
  private static void loadBuildings(BufferedReader bufferedReader) {
    String line;
    String[] items;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        items = line.split(",");
        String buildingNumber = items[0].trim();
        String name = items[1].trim();
        int nodeNumber = Integer.parseInt(items[2].trim());
        buildings.add(new Building(buildingNumber, name, nodeNumber));
      }
      bufferedReader.close();
    } catch (IOException exception) {
      Main.getLogger().fine("Exception!");
      exception.printStackTrace();
    }
  }


  /**
   * @param object The Building you are checking to be equal.
   * @return The list of buildings.
   */
  @Override
  public boolean equals(Object object) {
    if (buildingNumber == ((Building) object).buildingNumber) {
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
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

  /**
   * @return The string with '...' or padded with spaces.
   */
  public String padString() {
    String temp = name;
    int length = ABREVIATE_LENGTH;
    if (name.length() > length) {
      temp = name.substring(0, length - buildingNumber.length());
      temp = temp + "...";
    } else if (name.length() < length) {
      for (int i = 0; i < length; i++) {
        temp = temp + " ";
      }
    }
    return buildingNumber + " " + temp;
  }

}
