package deadzone.math;

/**
 * This class represents a (x,y,z,w)-Vector based on float values.
 * To make it easier to use with GLSLs vec4, the scalars are labeled (x,y,z,w) instead of (x1,x2,x,3,x4).
 */
public class Vector4f {
  
  public float x;
  public float y;
  public float z;
  public float w;
  
  /**
   * Creates a new vector with 0 for all values
   */
  public Vector4f() {
    this.x = 0f;
    this.y = 0f;
    this.z = 0f;
    this.w = 0f;
  }
  
  /**
   * Creates a new vector with the given values
   */
  public Vector4f(float x, float y, float z, float w) {
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
   * Adds this vector to another (x,y,z,w)-Vector
   */
  public Vector4f add(Vector4f vector) {
    float x = this.x + vector.x;
    float y = this.y + vector.y;
    float z = this.z + vector.z;
    float w = this.w + vector.w;
    return new Vector4f(x, y, z, w);
  }
  
  
  /**
   * Subtracts this vector from another (x,y,z,w)-Vector
   */
  public Vector4f subtract(Vector4f vector) {
    return this.add(vector.negate());
  }
  
  
  /**
   * Calculates the dot product of this vector with another (x,y,z,w)-Vector
   */
  public float dot(Vector4f vector) {
    return this.x * vector.x + this.y * vector.y + this.z * vector.z + this.w * vector.w;
  }
  
  
  /**
   * Negates this vector
   */
  public Vector4f negate() {
    return multiply(-1f);
  }
  
  
  /**
   * Multiplies ("scales") this vector with a scalar
   */
  public Vector4f multiply(float scalar) {
    float x = this.x * scalar;
    float y = this.y * scalar;
    float z = this.z * scalar;
    float w = this.w * scalar;
    return new Vector4f(x, y, z, w);
  }
  
  
  /**
   * Divides this vector with a scalar value
   */
  public Vector4f divide(float scalar) {
    return multiply(1f / scalar);
  }
  
  
  /**
   * Normalizes this vector (same direction with length of 1)
   */
  public Vector4f normalize() {
    float length = length();
    return divide(length);
  }
  
}
