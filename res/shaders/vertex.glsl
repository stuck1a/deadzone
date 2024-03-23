#version 330 core

in vec2 position;
in vec3 color;

out vec3 vertexColor;

void main() {
  vertexColor = color;
  gl_Position = vec3(position, 1.0);
}