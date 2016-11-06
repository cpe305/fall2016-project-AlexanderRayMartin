package alexanderraymartin.util;

public class Vector2i {

  private int xcoord;
  private int ycoord;

  public Vector2i() {
    set(0, 0);
  }

  public Vector2i(Vector2i vector) {
    set(vector.xcoord, vector.ycoord);
  }

  public Vector2i(int xcoord, int ycoord) {
    set(xcoord, ycoord);
  }

  public void set(int xcoord, int ycoord) {
    this.xcoord = xcoord;
    this.ycoord = ycoord;
  }

  /**
   * @param vector The vector to add.
   * @return The current instance of Vector2i.
   */
  public Vector2i add(Vector2i vector) {
    this.xcoord += vector.xcoord;
    this.ycoord += vector.ycoord;
    return this;
  }

  /**
   * @param vector The vector to subtract.
   * @return The current instance of Vector2i.
   */
  public Vector2i subtract(Vector2i vector) {
    this.xcoord -= vector.xcoord;
    this.ycoord -= vector.ycoord;
    return this;
  }

  public int getX() {
    return xcoord;
  }

  public int getY() {
    return ycoord;
  }

  public Vector2i setX(int xcoord) {
    this.xcoord = xcoord;
    return this;
  }

  public Vector2i setY(int ycoord) {
    this.ycoord = ycoord;
    return this;
  }

}
