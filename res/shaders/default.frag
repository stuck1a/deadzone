#version 150 core

in vec4 vertexColor;
in vec2 vertexTexcoord;

out vec4 outColor;

uniform sampler2D foo;
uniform sampler2D textureData;

void main() {
  vec4 textureColor = texture(textureData, vertexTexcoord);
  outColor = vertexColor * textureColor;
}

