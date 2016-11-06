package alexanderraymartin.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
  private static int mouseX = -1;
  private static int mouseY = -1;
  private static int mouseB = -1;
  private static boolean mouseClicked = false;
  private static boolean mouseReleased = false;

  public static int getX() {
    return mouseX;
  }

  public static int getY() {
    return mouseY;
  }


  /**
   * @return State of mouseB.
   */
  public static int getButton() {
    if (mouseB != -1) {
      mouseClicked = true;
      mouseReleased = false;
    }
    if (mouseB == -1 && mouseClicked) {
      mouseClicked = false;
      mouseReleased = true;
    }
    return mouseB;
  }

  /**
   * @return True if a button was clicked and released.
   */
  public static boolean buttonClickAndRelease() {
    boolean temp = mouseReleased;
    mouseReleased = false;
    return temp;
  }

  public void mouseDragged(MouseEvent event) {
    mouseX = event.getX();
    mouseY = event.getY();
  }

  public void mouseMoved(MouseEvent event) {
    mouseX = event.getX();
    mouseY = event.getY();
  }

  public void mouseClicked(MouseEvent event) {}

  public void mouseEntered(MouseEvent event) {}

  public void mouseExited(MouseEvent event) {}

  public void mousePressed(MouseEvent event) {
    mouseB = event.getButton();
  }

  public void mouseReleased(MouseEvent event) {
    mouseB = -1;
  }
}
