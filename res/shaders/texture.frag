// Variante für den fragment shader mit Texturen (wird aktuell noch mit den vertexColors verrechnet, aber im Grunde müsste die vertexColors komplett raus

#version 150 core

in vec4 vertexColor;
in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D texImage;

void main() {
  vec4 textureColor = texture(texImage, textureCoord);
  outColor = vertexColor * textureColor;
}
