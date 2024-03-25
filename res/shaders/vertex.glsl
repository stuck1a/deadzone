#version 150 core

in vec2 position;
in vec4 color;

out vec4 vertexColor;

void main() {
  vertexColor = color;
  gl_Position = vec4(position, 0.0, 1.0);
}
