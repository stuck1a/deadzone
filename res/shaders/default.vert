#version 150 core

in vec2 position;
in vec4 color;
in vec2 texcoord;

out vec4 vertexColor;
out vec2 vertexTexcoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
  vertexColor = color;
  vertexTexcoord = texcoord;
  mat4 mvp = projection * view * model;
  gl_Position = mvp * vec4(position, 0.0, 1.0);
}
