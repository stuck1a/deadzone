package deadzone.math;


/**
 * This class represents a 4x4 Matrix based on float values.
 */
public class Matrix4x4 {
  
  private float x00, x01, x02, x03;
  private float x10, x11, x12, x13;
  private float x20, x21, x22, x23;
  private float x30, x31, x32, x33;
  
  
  /**
   * Creates an orthographic projection matrix (equivalent to OpenGL glOrtho)
   * @param left   Coordinate for the left vertical clipping pane
   * @param right  Coordinate for the right vertical clipping pane
   * @param bottom Coordinate for the bottom horizontal clipping pane
   * @param top    Coordinate for the bottom horizontal clipping pane
   * @param near   Coordinate for the near depth clipping pane
   * @param far    Coordinate for the far depth clipping pane
   */
  public static Matrix4x4 createOrthoProjectionMatrix(float left, float right, float bottom, float top, float near, float far) {
    Matrix4x4 ortho = new Matrix4x4();
    float tx = -(right + left) / (right - left);
    float ty = -(top + bottom) / (top - bottom);
    float tz = -(far + near) / (far - near);
    ortho.x00 = 2f / (right - left);
    ortho.x11 = 2f / (top - bottom);
    ortho.x22 = -2f / (far - near);
    ortho.x03 = tx;
    ortho.x13 = ty;
    ortho.x23 = tz;
    return ortho;
  }
  
  /**
   * Creates a translation matrix (equivalent to OpenGL glTranslate)
   * @param x x coordinate of translation vector
   * @param y y coordinate of translation vector
   * @param z z coordinate of translation vector
   */
  public static Matrix4x4 createTranslationMatrix(float x, float y, float z) {
    Matrix4x4 trans = new Matrix4x4();
    trans.x03 = x;
    trans.x13 = y;
    trans.x23 = z;
    return trans;
  }
  
  /**
   * Creates a rotation matrix (equivalent to OpenGL glRotate)
   * @param angle Angle of rotation in degrees
   * @param x     x coordinate of the rotation vector
   * @param y     y coordinate of the rotation vector
   * @param z     z coordinate of the rotation vector
   */
  public static Matrix4x4 createRotationMatrix(float angle, float x, float y, float z) {
    Matrix4x4 rot = new Matrix4x4();
    float c = (float) Math.cos(Math.toRadians(angle));
    float s = (float) Math.sin(Math.toRadians(angle));
    Vector3f vec = new Vector3f(x, y, z);
    if (vec.length() != 1f) {
      vec = vec.normalize();
      x = vec.x;
      y = vec.y;
      z = vec.z;
    }
    rot.x00 = x * x * (1f - c) + c;
    rot.x10 = y * x * (1f - c) + z * s;
    rot.x20 = x * z * (1f - c) - y * s;
    rot.x01 = x * y * (1f - c) - z * s;
    rot.x11 = y * y * (1f - c) + c;
    rot.x21 = y * z * (1f - c) + x * s;
    rot.x02 = x * z * (1f - c) + y * s;
    rot.x12 = y * z * (1f - c) - x * s;
    rot.x22 = z * z * (1f - c) + c;
    return rot;
  }
  
  /**
   * Creates a scaling matrix (equivalent to OpenGL glScale)
   * @param x Scale factor along the x coordinate
   * @param y Scale factor along the y coordinate
   * @param z Scale factor along the z coordinate
   */
  public static Matrix4x4 createScalingMatrix(float x, float y, float z) {
    Matrix4x4 scaler = new Matrix4x4();
    scaler.x00 = x;
    scaler.x11 = y;
    scaler.x22 = z;
    return scaler;
  }
  
  
  /**
   * Creates a 3x3 identity ("neutral") matrix
   */
  public Matrix4x4() {
    setIdentity();
  }
  
  /**
   * Creates a 4x4 matrix from 16 scalars
   */
  public Matrix4x4(float x00, float x01, float x02, float x03, float x10, float x11, float x12, float x13, float x20, float x21, float x22, float x23, float x30, float x31, float x32, float x33) {
    this.x00 = x00;
    this.x01 = x01;
    this.x02 = x02;
    this.x03 = x03;
    this.x10 = x10;
    this.x11 = x11;
    this.x12 = x12;
    this.x13 = x13;
    this.x20 = x20;
    this.x21 = x21;
    this.x22 = x22;
    this.x23 = x23;
    this.x30 = x30;
    this.x31 = x31;
    this.x32 = x32;
    this.x33 = x33;
  }
  
  /**
   * Creates a 4x4 matrix from four (x,y,z,w)-Vectors as columns
   */
  public Matrix4x4(Vector4f col1, Vector4f col2, Vector4f col3, Vector4f col4) {
    x00 = col1.x;
    x10 = col1.y;
    x20 = col1.z;
    x30 = col1.w;
    x01 = col2.x;
    x11 = col2.y;
    x21 = col2.z;
    x31 = col2.w;
    x02 = col3.x;
    x12 = col3.y;
    x22 = col3.z;
    x32 = col3.w;
    x03 = col4.x;
    x13 = col4.y;
    x23 = col4.z;
    x33 = col4.w;
  }
  
  /**
   * Rewrite this matrix to an identity ("neutral") matrix
   */
  public final void setIdentity() {
    x00 = 1f;
    x01 = 0f;
    x02 = 0f;
    x03 = 0f;
    x10 = 0f;
    x11 = 1f;
    x12 = 0f;
    x13 = 0f;
    x20 = 0f;
    x21 = 0f;
    x22 = 1f;
    x23 = 0f;
    x30 = 0f;
    x31 = 0f;
    x32 = 0f;
    x33 = 1f;
  }
  
  /**
   * Adds a 4x4 matrix to this matrix (this + param)
   */
  public Matrix4x4 add(Matrix4x4 matrix) {
    Matrix4x4 result = new Matrix4x4();
    result.x00 = this.x00 + matrix.x00;
    result.x01 = this.x01 + matrix.x01;
    result.x02 = this.x02 + matrix.x02;
    result.x03 = this.x03 + matrix.x03;
    result.x10 = this.x10 + matrix.x10;
    result.x11 = this.x11 + matrix.x11;
    result.x12 = this.x12 + matrix.x12;
    result.x13 = this.x13 + matrix.x13;
    result.x20 = this.x20 + matrix.x20;
    result.x21 = this.x21 + matrix.x21;
    result.x22 = this.x22 + matrix.x22;
    result.x23 = this.x23 + matrix.x23;
    result.x30 = this.x30 + matrix.x30;
    result.x31 = this.x31 + matrix.x31;
    result.x32 = this.x32 + matrix.x32;
    result.x33 = this.x33 + matrix.x33;
    return result;
  }
  
  /**
   * Negates this matrix
   */
  public Matrix4x4 negate() {
    return multiply(-1f);
  }
  
  /**
   * Subtracts a 4x4 matrix from this matrix (this - param)
   */
  public Matrix4x4 subtract(Matrix4x4 other) {
    return this.add(other.negate());
  }
  
  /**
   * Multiplies a scalar to this matrix (this * param)
   */
  public Matrix4x4 multiply(float scalar) {
    Matrix4x4 result = new Matrix4x4();
    result.x00 = this.x00 * scalar;
    result.x01 = this.x01 * scalar;
    result.x02 = this.x02 * scalar;
    result.x03 = this.x03 * scalar;
    result.x10 = this.x10 * scalar;
    result.x11 = this.x11 * scalar;
    result.x12 = this.x12 * scalar;
    result.x13 = this.x13 * scalar;
    result.x20 = this.x20 * scalar;
    result.x21 = this.x21 * scalar;
    result.x22 = this.x22 * scalar;
    result.x23 = this.x23 * scalar;
    result.x30 = this.x30 * scalar;
    result.x31 = this.x31 * scalar;
    result.x32 = this.x32 * scalar;
    result.x33 = this.x33 * scalar;
    return result;
  }
  
  /**
   * Multiplies a (x,y,z,w)-Vector to this matrix (this * param)
   */
  public Vector4f multiply(Vector4f vector) {
    float x = this.x00 * vector.x + this.x01 * vector.y + this.x02 * vector.z + this.x03 * vector.w;
    float y = this.x10 * vector.x + this.x11 * vector.y + this.x12 * vector.z + this.x13 * vector.w;
    float z = this.x20 * vector.x + this.x21 * vector.y + this.x22 * vector.z + this.x23 * vector.w;
    float w = this.x30 * vector.x + this.x31 * vector.y + this.x32 * vector.z + this.x33 * vector.w;
    return new Vector4f(x, y, z, w);
  }
  
  /**
   * Multiplies a 4x4 matrix this matrix (this * param)
   */
  public Matrix4x4 multiply(Matrix4x4 matrix) {
    Matrix4x4 result = new Matrix4x4();
    result.x00 = this.x00 * matrix.x00 + this.x01 * matrix.x10 + this.x02 * matrix.x20 + this.x03 * matrix.x30;
    result.x01 = this.x00 * matrix.x01 + this.x01 * matrix.x11 + this.x02 * matrix.x21 + this.x03 * matrix.x31;
    result.x02 = this.x00 * matrix.x02 + this.x01 * matrix.x12 + this.x02 * matrix.x22 + this.x03 * matrix.x32;
    result.x03 = this.x00 * matrix.x03 + this.x01 * matrix.x13 + this.x02 * matrix.x23 + this.x03 * matrix.x33;
    result.x10 = this.x10 * matrix.x00 + this.x11 * matrix.x10 + this.x12 * matrix.x20 + this.x13 * matrix.x30;
    result.x11 = this.x10 * matrix.x01 + this.x11 * matrix.x11 + this.x12 * matrix.x21 + this.x13 * matrix.x31;
    result.x12 = this.x10 * matrix.x02 + this.x11 * matrix.x12 + this.x12 * matrix.x22 + this.x13 * matrix.x32;
    result.x13 = this.x10 * matrix.x03 + this.x11 * matrix.x13 + this.x12 * matrix.x23 + this.x13 * matrix.x33;
    result.x20 = this.x20 * matrix.x00 + this.x21 * matrix.x10 + this.x22 * matrix.x20 + this.x23 * matrix.x30;
    result.x21 = this.x20 * matrix.x01 + this.x21 * matrix.x11 + this.x22 * matrix.x21 + this.x23 * matrix.x31;
    result.x22 = this.x20 * matrix.x02 + this.x21 * matrix.x12 + this.x22 * matrix.x22 + this.x23 * matrix.x32;
    result.x23 = this.x20 * matrix.x03 + this.x21 * matrix.x13 + this.x22 * matrix.x23 + this.x23 * matrix.x33;
    result.x30 = this.x30 * matrix.x00 + this.x31 * matrix.x10 + this.x32 * matrix.x20 + this.x33 * matrix.x30;
    result.x31 = this.x30 * matrix.x01 + this.x31 * matrix.x11 + this.x32 * matrix.x21 + this.x33 * matrix.x31;
    result.x32 = this.x30 * matrix.x02 + this.x31 * matrix.x12 + this.x32 * matrix.x22 + this.x33 * matrix.x32;
    result.x33 = this.x30 * matrix.x03 + this.x31 * matrix.x13 + this.x32 * matrix.x23 + this.x33 * matrix.x33;
    return result;
  }
  
  /**
   * Transposes this matrix
   */
  public Matrix4x4 transpose() {
    Matrix4x4 result = new Matrix4x4();
    result.x00 = this.x00;
    result.x01 = this.x10;
    result.x02 = this.x20;
    result.x03 = this.x30;
    result.x10 = this.x01;
    result.x11 = this.x11;
    result.x12 = this.x21;
    result.x13 = this.x31;
    result.x20 = this.x02;
    result.x21 = this.x12;
    result.x22 = this.x22;
    result.x23 = this.x32;
    result.x30 = this.x03;
    result.x31 = this.x13;
    result.x32 = this.x23;
    result.x33 = this.x33;
    return result;
  }
  
}
