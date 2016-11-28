package alexanderraymartin.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Alex Martin.
 *
 */
public class Save {

  /**
   * The directory path.
   */
  public static final String DIRECTORY = System.getProperty("user.home") + "/Documents/Poly Path/";
  /**
   * The name of the save file.
   */
  public static final String SAVE_PATH = "save.data";
  /**
   * The static instance of Save.
   */
  private static Save save;

  /**
   * Creates the save file and directory if it does not exist.
   */
  private Save() {
    createDirectory();
    loadSchedule();
  }

  /**
   * @return The instance of save.
   */
  public static Save getInstance() {
    if (save == null) {
      save = new Save();
    }
    return save;
  }

  /**
   * Save the schedule to the output file.
   */
  public void saveSchedule() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(DIRECTORY + SAVE_PATH);
      ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
      outputStream.writeObject(Schedule.getInstance());
      outputStream.close();
      fileOutputStream.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Load the schedule.
   */
  private void loadSchedule() {
    File file = new File(DIRECTORY + SAVE_PATH);
    if (!file.exists()) {
      saveSchedule();
    }
    try {
      FileInputStream fileInputStream = new FileInputStream(DIRECTORY + SAVE_PATH);
      ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
      Schedule.getInstance().setSchedule((Schedule) inputStream.readObject());
      inputStream.close();
      fileInputStream.close();
    } catch (IOException | ClassNotFoundException exception) {
      exception.printStackTrace();
      System.out.println("Corrupted save file");
      saveSchedule();
    }
  }

  /**
   * Create the directory for the save file.
   */
  private void createDirectory() {
    File file = new File(DIRECTORY);
    {
      if (!file.exists()) {
        System.out.println("Creating: " + file);
        boolean successful = file.mkdirs();
        if (successful) {
          System.out.println("Folders created!");
        }
      }
    }
  }

}
