#version 150 core

in vec2 position;
in vec4 color;

out vec4 vertexColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
  vertexColor = color;
  mat4 mvp = projection * view * model;
  gl_Position = mvp * vec4(position, 0.0, 1.0);
}
