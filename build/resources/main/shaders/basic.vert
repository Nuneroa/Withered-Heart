#version 330 core
layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;

out vec2 vTexCoord;

void main() {
    gl_Position = projection * model * vec4(position, 0.0, 1.0);
    vTexCoord = texCoord;
}
