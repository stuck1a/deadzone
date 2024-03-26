package deadzone.math;

/**
 * This class represents a (x,y)-Vector based on float values.
 */
public class Vector2f {
  
  public float x;
  public float y;
  
  
  /**
   * Creates a new vector with 0 for both values
   */
  public Vector2f() {
    this.x = 0f;
    this.y = 0f;
  }
  
  /**
   * Creates a new vector with the given values
   */
  public Vector2f(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  
  /**
   * Calculates the scalar length of this vector
   */
  public float length() {
    return (float) Math.sqrt(x*x + y*y);
  }
  
  
  /**
   * Adds this vector to another (x,y)-Vector
   */
  public Vector2f add(Vector2f vector) {
    float x = this.x + vector.x;
    float y = this.y + vector.y;
    return new Vector2f(x, y);
  }
  
  
  /**
   * Subtracts this vector from another (x,y)-Vector
   */
  public Vector2f subtract(Vector2f vector) {
    return this.add(vector.negate());
  }
  
  
  /**
   * Calculates the dot product of this vector with another (x,y)-Vector
   */
  public float dot(Vector2f vector) {
    return this.x * vector.x + this.y * vector.y;
  }
  
  /**
   * Negates this vector
   */
  public Vector2f negate() {
    return multiply(-1f);
  }
  
  
  /**
   * Multiplies ("scales") this vector with a scalar
   */
  public Vector2f multiply(float scalar) {
    float x = this.x * scalar;
    float y = this.y * scalar;
    return new Vector2f(x, y);
  }
  
  
  /**
   * Divides this vector with a scalar value
   */
  public Vector2f divide(float scalar) {
    return multiply(1f / scalar);
  }
  
  
  /**
   * Normalizes this vector (same direction with length of 1)
   */
  public Vector2f normalize() {
    float length = length();
    return divide(length);
  }
  
}
