package deadzone.math;


/**
 * This class represents a (x,y)-Vector based on float values.
 */
public class Vector2 {
  
  public float x;
  public float y;
  
  
  /**
   * Creates a new vector with 0 for both values
   */
  public Vector2() {
    this.x = 0f;
    this.y = 0f;
  }
  
  /**
   * Creates a new vector with the given values
   */
  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  
  /**
   * Calculates the squared scalar length of this vector
   */
  public float lengthSquared() {
    return x * x + y * y;
  }
  
  /**
   * Calculates the scalar length of this vector
   */
  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }
  
  /**
   * Adds a (x,y)-Vector to this vector (this + param)
   */
  public Vector2 add(Vector2 vector) {
    float x = this.x + vector.x;
    float y = this.y + vector.y;
    return new Vector2(x, y);
  }
  
  /**
   * Subtracts a (x,y)-Vector from this vector (this - param)
   */
  public Vector2 subtract(Vector2 vector) {
    return this.add(vector.negate());
  }
  
  /**
   * Calculates the dot product of this vector with another (x,y)-Vector (this * param)
   */
  public float dot(Vector2 vector) {
    return this.x * vector.x + this.y * vector.y;
  }
  
  /**
   * Negates this vector
   */
  public Vector2 negate() {
    return multiply(-1f);
  }
  
  /**
   * Multiplies ("scales") a scalar to this vector (this * param)
   */
  public Vector2 multiply(float scalar) {
    float x = this.x * scalar;
    float y = this.y * scalar;
    return new Vector2(x, y);
  }
  
  /**
   * Divides a scalar value from this vector (this / param)
   */
  public Vector2 divide(float scalar) {
    return multiply(1f / scalar);
  }
  
  /**
   * Normalizes this vector (same direction with length of 1)
   */
  public Vector2 normalize() {
    float length = length();
    return divide(length);
  }
  
}
