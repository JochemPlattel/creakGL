package creakGL

import org.lwjgl.glfw.GLFW.glfwGetTime

object Time {
    var frameCount = 0
        private set
    var time = 0f
        private set
    var unscaledTime = 0f
        private set
    var timeScale = 1f

    var deltaTime = 0f
        private set
    var unscaledDeltaTime = 0f

    private var lastTime = 0.0
    var glfwTime  = 0.0
    fun updateTime() {
        glfwTime = glfwGetTime()
        unscaledTime = glfwTime.toFloat()
        unscaledDeltaTime = (glfwTime - lastTime).toFloat()
        deltaTime = unscaledDeltaTime * timeScale
        time += deltaTime
        lastTime = glfwTime
        frameCount++
    }
}