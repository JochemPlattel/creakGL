package creakGL

import org.lwjgl.opengl.GL

var isGLInit = false
fun init() {
    GL.createCapabilities()
    isGLInit = true
    generateGLObjects()
}