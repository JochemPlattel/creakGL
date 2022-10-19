#version 400

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec4 aColor;
out vec4 color;

void main() {
    color = aColor;
    gl_Position = vec4(aPosition, 0, 1);
}