package alexanderRayMartin.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Save {

    public static final String DIRECTORY = System.getProperty("user.home") + "/Documents/Poly Path/";
    public static final String SAVE_PATH = "save.data";
    public static Save save;

    /** Creates the save file and directory if it does not exist */
    private Save() {
        createDirectory();
        loadSchedule();
    }

    /** returns the instance of save */
    public static Save getInstance() {
        if (save == null)
            save = new Save();
        return save;
    }

    /** save the preferences */
    public void saveSchedule() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(DIRECTORY + SAVE_PATH);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(Schedule.getInstance());
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** load the preferences */
    private void loadSchedule() {
        File file = new File(DIRECTORY + SAVE_PATH);
        if (!file.exists())
            saveSchedule();
        try {

            FileInputStream fileInputStream = new FileInputStream(DIRECTORY + SAVE_PATH);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            Schedule.getInstance().setSchedule((Schedule) inputStream.readObject());
            inputStream.close();
            fileInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Corrupted save file");
            saveSchedule();
        }
    }

    /** create the directory for the save files */
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
