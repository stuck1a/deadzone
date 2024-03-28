package deadzone.math;


/**
 * This class represents a 3x3 Matrix based on float values.
 */
public class Matrix3x3 {
  
  private float x00, x01, x02;
  private float x10, x11, x12;
  private float x20, x21, x22;
  
  
  /**
   * Creates a 3x3 identity ("neutral") matrix
   */
  public Matrix3x3() {
    setIdentity();
  }
  
  /**
   * Creates a 3x3 matrix from nine scalars
   */
  public Matrix3x3(float x00, float x01, float x10, float x11) {
    this.x00 = x00;
    this.x10 = x01;
    this.x01 = x10;
    this.x11 = x11;
  }
  
  /**
   * Creates a 3x3 matrix from three (x,y,z)-Vectors as columns
   */
  public Matrix3x3(Vector3f col1, Vector3f col2, Vector3f col3) {
    x00 = col1.x;
    x10 = col1.y;
    x20 = col1.z;
    x01 = col2.x;
    x11 = col2.y;
    x21 = col2.z;
    x02 = col3.x;
    x12 = col3.y;
    x22 = col3.z;
  }
  
  
  /**
   * Rewrite this matrix to an identity ("neutral") matrix
   */
  public void setIdentity() {
    x00 = 1f;
    x01 = 0f;
    x02 = 0f;
    x10 = 0f;
    x11 = 1f;
    x12 = 0f;
    x20 = 0f;
    x21 = 0f;
    x22 = 1f;
  }
  
  /**
   * Adds another 3x3 matrix to this matrix (this + param)
   */
  public Matrix3x3 add(Matrix3x3 matrix) {
    Matrix3x3 result = new Matrix3x3();
    result.x00 = this.x00 + matrix.x00;
    result.x01 = this.x01 + matrix.x01;
    result.x02 = this.x02 + matrix.x02;
    result.x10 = this.x10 + matrix.x10;
    result.x11 = this.x11 + matrix.x11;
    result.x12 = this.x12 + matrix.x12;
    result.x20 = this.x20 + matrix.x20;
    result.x21 = this.x21 + matrix.x21;
    result.x22 = this.x22 + matrix.x22;
    return result;
  }
  
  /**
   * Negates this matrix
   */
  public Matrix3x3 negate() {
    return multiply(-1f);
  }
  
  /**
   * Subtracts another 3x3 matrix from this matrix (this - param)
   */
  public Matrix3x3 subtract(Matrix3x3 matrix) {
    return this.add(matrix.negate());
  }
  
  /**
   * Multiplies a scalar value to this matrix (this * param)
   */
  public Matrix3x3 multiply(float scalar) {
    Matrix3x3 result = new Matrix3x3();
    result.x00 = this.x00 * scalar;
    result.x01 = this.x01 * scalar;
    result.x02 = this.x02 * scalar;
    result.x10 = this.x10 * scalar;
    result.x11 = this.x11 * scalar;
    result.x12 = this.x12 * scalar;
    result.x20 = this.x20 * scalar;
    result.x21 = this.x21 * scalar;
    result.x22 = this.x22 * scalar;
    return result;
  }
  
  /**
   * Multiplies a (x,y,z)-Vector to this matrix
   */
  public Vector3f multiply(Vector3f vector) {
    float x = this.x00 * vector.x + this.x01 * vector.y + this.x02 * vector.z;
    float y = this.x10 * vector.x + this.x11 * vector.y + this.x12 * vector.z;
    float z = this.x20 * vector.x + this.x21 * vector.y + this.x22 * vector.z;
    return new Vector3f(x, y, z);
  }
  
  /**
   * Multiplies a 3x3 matrix to this matrix (this * param)
   */
  public Matrix3x3 multiply(Matrix3x3 matrix) {
    Matrix3x3 result = new Matrix3x3();
    result.x00 = this.x00 * matrix.x00 + this.x01 * matrix.x10 + this.x02 * matrix.x20;
    result.x01 = this.x00 * matrix.x01 + this.x01 * matrix.x11 + this.x02 * matrix.x21;
    result.x02 = this.x00 * matrix.x02 + this.x01 * matrix.x12 + this.x02 * matrix.x22;
    result.x10 = this.x10 * matrix.x00 + this.x11 * matrix.x10 + this.x12 * matrix.x20;
    result.x11 = this.x10 * matrix.x01 + this.x11 * matrix.x11 + this.x12 * matrix.x21;
    result.x12 = this.x10 * matrix.x02 + this.x11 * matrix.x12 + this.x12 * matrix.x22;
    result.x20 = this.x20 * matrix.x00 + this.x21 * matrix.x10 + this.x22 * matrix.x20;
    result.x21 = this.x20 * matrix.x01 + this.x21 * matrix.x11 + this.x22 * matrix.x21;
    result.x22 = this.x20 * matrix.x02 + this.x21 * matrix.x12 + this.x22 * matrix.x22;
    return result;
  }
  
  /**
   * Transposes this matrix
   */
  public Matrix3x3 transpose() {
    Matrix3x3 result = new Matrix3x3();
    result.x00 = this.x00;
    result.x10 = this.x01;
    result.x01 = this.x10;
    result.x11 = this.x11;
    return result;
  }
  
}
