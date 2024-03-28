package deadzone.math;

/**
 * This class represents a (x,y,z)-Vector based on float values.
 */
public class Vector3 {
  
  public float x;
  public float y;
  public float z;
  
  
  /**
   * Creates a new vector with 0 for all values
   */
  public Vector3() {
    this.x = 0f;
    this.y = 0f;
    this.z = 0f;
  }
  
  /**
   * Creates a new vector with the given values
   */
  public Vector3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  
  /**
   * Calculates the squared scalar length of this vector
   */
  public float lengthSquared() {
    return x * x + y * y + z * z;
  }
  
  /**
   * Calculates the scalar length of this vector
   */
  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }
  
  /**
   * Adds a (x,y,z)-Vector to this vector (this + param)
   */
  public Vector3 add(Vector3 vector) {
    float x = this.x + vector.x;
    float y = this.y + vector.y;
    float z = this.z + vector.z;
    return new Vector3(x, y, z);
  }
  
  /**
   * Subtracts a (x,y,z)-Vector from this vector (this - param)
   */
  public Vector3 subtract(Vector3 vector) {
    return this.add(vector.negate());
  }
  
  /**
   * Calculates the dot product of this vector with another (x,y,z)-Vector (this * param)
   */
  public float dot(Vector3 vector) {
    return this.x * vector.x + this.y * vector.y + this.z * vector.z;
  }
  
  /**
   * Calculates the cross product of this vector with another (x,y,z)-Vector (this X param)
   */
  public Vector3 cross(Vector3 vector) {
    float x = this.y * vector.z - this.z * vector.y;
    float y = this.z * vector.x - this.x * vector.z;
    float z = this.x * vector.y - this.y * vector.x;
    return new Vector3(x, y, z);
  }
  
  /**
   * Negates this vector
   */
  public Vector3 negate() {
    return multiply(-1f);
  }
  
  /**
   * Multiplies ("scales") a scalar to this vector (this * param)
   */
  public Vector3 multiply(float scalar) {
    float x = this.x * scalar;
    float y = this.y * scalar;
    float z = this.z * scalar;
    return new Vector3(x, y, z);
  }
  
  /**
   * Divides a scalar value from this vector (this / param)
   */
  public Vector3 divide(float scalar) {
    return multiply(1f / scalar);
  }
  
  /**
   * Normalizes this vector (same direction with length of 1)
   */
  public Vector3 normalize() {
    float length = length();
    return divide(length);
  }
  
}
