package deadzone.math;

/**
 * This class represents a (x,y,z,w)-Vector based on float values.
 * To make it easier to use with GLSLs vec4, the scalars are labeled (x,y,z,w) instead of (x1,x2,x,3,x4).
 */
public class Vector4 {
  
  public float x;
  public float y;
  public float z;
  public float w;
  
  
  /**
   * Creates a new vector with 0 for all values
   */
  public Vector4() {
    this.x = 0f;
    this.y = 0f;
    this.z = 0f;
    this.w = 0f;
  }
  
  /**
   * Creates a new vector with the given values
   */
  public Vector4(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  
  
  /**
   * Calculates the squared scalar length of this vector
   */
  public float lengthSquared() {
    return x * x + y * y + z * z + w * w;
  }
  
  /**
   * Calculates the scalar length of this vector
   */
  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }
  
  /**
   * Adds a (x,y,z,w)-Vector to this vector (this + param)
   */
  public Vector4 add(Vector4 vector) {
    float x = this.x + vector.x;
    float y = this.y + vector.y;
    float z = this.z + vector.z;
    float w = this.w + vector.w;
    return new Vector4(x, y, z, w);
  }
  
  /**
   * Subtracts a (x,y,z,w)-Vector from this vector (this - param)
   */
  public Vector4 subtract(Vector4 vector) {
    return this.add(vector.negate());
  }
  
  /**
   * Calculates the dot product of this vector with another (x,y,z,w)-Vector (this * param)
   */
  public float dot(Vector4 vector) {
    return this.x * vector.x + this.y * vector.y + this.z * vector.z + this.w * vector.w;
  }
  
  /**
   * Negates this vector
   */
  public Vector4 negate() {
    return multiply(-1f);
  }
  
  /**
   * Multiplies ("scales") a scalar to this vector (this * param)
   */
  public Vector4 multiply(float scalar) {
    float x = this.x * scalar;
    float y = this.y * scalar;
    float z = this.z * scalar;
    float w = this.w * scalar;
    return new Vector4(x, y, z, w);
  }
  
  /**
   * Divides a scalar value from this vector (this / param)
   */
  public Vector4 divide(float scalar) {
    return multiply(1f / scalar);
  }
  
  /**
   * Normalizes this vector (same direction with length of 1)
   */
  public Vector4 normalize() {
    float length = length();
    return divide(length);
  }
  
}
