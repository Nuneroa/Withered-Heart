#version 330 core

in vec2 TexCoords;
out vec4 fragColor;

uniform sampler2D text;
uniform vec4 textColor;

void main() {
    float sampled = texture(text, TexCoords).r;
    fragColor = vec4(textColor.rgb, textColor.a * sampled);
}
