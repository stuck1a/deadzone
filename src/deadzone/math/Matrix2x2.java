package deadzone.math;


/**
 * This class represents a 2x2 Matrix based on float values.
 */
public class Matrix2x2 {

  private float x00, x01;
  private float x10, x11;
  
  
  /**
   * Creates a 2x2 identity ("neutral") matrix
   */
  public Matrix2x2() {
    setIdentity();
  }
  
  
  /**
   * Creates a 2x2 matrix from four scalars
   */
  public Matrix2x2(float x00, float x01, float x10, float x11) {
    this.x00 = x00;
    this.x10 = x01;
    this.x01 = x10;
    this.x11 = x11;
  }
  
  
  /**
   * Creates a 2x2 matrix from two (x,y)-Vectors as columns
   */
  public Matrix2x2(Vector2f col1, Vector2f col2) {
    this.x00 = col1.x;
    this.x10 = col1.y;
    this.x01 = col2.x;
    this.x11 = col2.y;
  }
  
  
  /**
   * Rewrite this matrix to an identity ("neutral") matrix
   */
  public void setIdentity() {
    this.x00 = 1f;
    this.x11 = 1f;
    this.x01 = 0f;
    this.x10 = 0f;
  }
  
  
  /**
   * Adds another 2x2 matrix to this matrix (this + param)
   */
  public Matrix2x2 add(Matrix2x2 matrix) {
    Matrix2x2 result = new Matrix2x2();
    result.x00 = this.x00 + matrix.x00;
    result.x10 = this.x10 + matrix.x10;
    result.x01 = this.x01 + matrix.x01;
    result.x11 = this.x11 + matrix.x11;
    return result;
  }
  
  
  /**
   * Negates this matrix
   */
  public Matrix2x2 negate() {
    return multiply(-1f);
  }
  
  
  /**
   * Subtracts another 2x2 matrix from this matrix (this - param)
   */
  public Matrix2x2 subtract(Matrix2x2 matrix) {
    return this.add(matrix.negate());
  }
  
  
  /**
   * Multiplies a scalar value to this matrix (this * param)
   */
  public Matrix2x2 multiply(float scalar) {
    Matrix2x2 result = new Matrix2x2();
    result.x00 = this.x00 * scalar;
    result.x10 = this.x10 * scalar;
    result.x01 = this.x01 * scalar;
    result.x11 = this.x11 * scalar;
    return result;
  }
  
  
  /**
   * Multiplies a (x,y)-Vector to this matrix
   */
  public Vector2f multiply(Vector2f vector) {
    float x = this.x00 * vector.x + this.x01 * vector.y;
    float y = this.x10 * vector.x + this.x11 * vector.y;
    return new Vector2f(x, y);
  }
  
  
  /**
   * Multiplies a 2x2 matrix to this matrix (this * param)
   */
  public Matrix2x2 multiply(Matrix2x2 matrix) {
    Matrix2x2 result = new Matrix2x2();
    result.x00 = this.x00 * matrix.x00 + this.x01 * matrix.x10;
    result.x10 = this.x10 * matrix.x00 + this.x11 * matrix.x10;
    result.x01 = this.x00 * matrix.x01 + this.x01 * matrix.x11;
    result.x11 = this.x10 * matrix.x01 + this.x11 * matrix.x11;
    return result;
  }
  
  
  /**
   * Transposes this matrix
   */
  public Matrix2x2 transpose() {
    Matrix2x2 result = new Matrix2x2();
    result.x00 = this.x00;
    result.x10 = this.x01;
    result.x01 = this.x10;
    result.x11 = this.x11;
    return result;
  }
  
}
