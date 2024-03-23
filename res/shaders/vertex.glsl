#version 150 core

in vec3 position;
in vec3 color;

out vec3 vertexColor;

void main() {
  vertexColor = color;
  gl_Position = vec4(position, 1.0);
}