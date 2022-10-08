package creakGL

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

object Window2 {
    var glfwWindow = 0L
    fun create(
        width: Int,
        height: Int,
        title: String = "New CreaK Window",
        vSyncEnabled: Boolean = false,
    ) {
        glfwInit()
    }
}

object Window {
    var glfwWindow = 0L
    var width = 1280
        private set
    var height = 720
        private set
    var aspectRatio = width.toFloat() / height

    fun create(
        width: Int,
        height: Int,
        title: String = "My Creak Project",
        resizable: Boolean = false,
        antiAliasingSamples: Int = 8,
        vSyncEnabled: Boolean = true,
    ): Window {
        this.width = width
        this.height = height
        aspectRatio = width.toFloat() / height

        glfwInit()
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_RESIZABLE, if (resizable) 1 else 0)
        glfwWindowHint(GLFW_SAMPLES, antiAliasingSamples)

        glfwWindow = glfwCreateWindow(width, height, title, 0L, 0L)
        glfwMakeContextCurrent(glfwWindow)
        glfwSwapInterval(if (vSyncEnabled) 1 else 0)
        glfwShowWindow(glfwWindow)
        init()
        glClearColor(0f, 0f, 0f, 1f)
        return this
    }

    fun run(updateFunction: () -> Unit) {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents()

            glClear(GL_COLOR_BUFFER_BIT)

            updateFunction()

            glfwSwapBuffers(glfwWindow)
        }
    }
}