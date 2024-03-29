#version 150 core

in vec2 position;
in vec4 color;

out vec4 vertexColor;

uniform mat4 MVP;

void main() {
  vertexColor = color;
  gl_Position = MVP * vec4(position, 0.0, 1.0);
}
