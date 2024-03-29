// NEW SHADER WITH TEXTURES

//#version 150 core
//
//in vec4 vertexColor;
//in vec2 textureCoord;
//
//out vec4 outColor;
//
//uniform sampler2D texImage;
//
//void main() {
//  vec4 textureColor = texture(texImage, textureCoord);
//  outColor = vertexColor * textureColor;
//}


// OLD SHADER WITHOUT TEXTURES

#version 150 core

in vec4 vertexColor;

out vec4 outColor;

void main() {
  outColor = vertexColor;
}
