#version 150 core

in vec3 vertexColor;

out vec4 outColor;

void main() {
  outColor = vec4(vertexColor, 1.0);
}