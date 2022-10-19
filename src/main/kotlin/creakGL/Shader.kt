package creakGL

import org.lwjgl.opengl.GL20.*
import java.io.File

var activeShaderProgram: ShaderProgram? = null

class FragmentShader(val source: String, var debugName: String = "unnamed") {
    var isCompiled = false
    var id = 0
    fun compile() {
        id = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(id, source)
        glCompileShader(id)
        var status = glGetShaderi(id, GL_COMPILE_STATUS)
        if (status == GL_FALSE) {
            val len = glGetShaderi(id, GL_INFO_LOG_LENGTH)
            throw Exception("FragmentShader '${debugName}' compilation failed: " + glGetShaderInfoLog(id, len))
        }
        isCompiled = true
    }
    companion object {
        fun load(filepath: String): FragmentShader {
            return FragmentShader(File(filepath).readText())
        }
    }
}

class VertexShader(val source: String, var debugName: String = "unnamed") {
    var isCompiled = false
    var id = 0
    fun compile() {
        id = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(id, source)
        glCompileShader(id)
        var status = glGetShaderi(id, GL_COMPILE_STATUS)
        if (status == GL_FALSE) {
            val len = glGetShaderi(id, GL_INFO_LOG_LENGTH)
            throw Exception("VertexShader '${debugName}' compilation failed: " + glGetShaderInfoLog(id, len))
        }
        isCompiled = true
    }

    companion object {
        fun load(filepath: String): VertexShader {
            return VertexShader(File(filepath).readText())
        }
    }
}

class ShaderProgram(
    val vertexShader: VertexShader,
    val fragmentShader: FragmentShader,
    val debugName: String = "unnamed"
) {
    var isUsed = false
    private var id = 0
    private val uniforms = mutableMapOf<String, Any>()

    fun compileAndLink() {
        if (!vertexShader.isCompiled)
            vertexShader.compile()
        if (!fragmentShader.isCompiled)
            fragmentShader.compile()
        id = glCreateProgram()
        glAttachShader(id, vertexShader.id)
        glAttachShader(id, fragmentShader.id)
        glLinkProgram(id)
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            val len = glGetProgrami(id, GL_INFO_LOG_LENGTH)
            throw Exception("ShaderProgram '${debugName}' linking failed" + glGetProgramInfoLog(id, len))
        }
        glDeleteShader(vertexShader.id)
        glDeleteShader(fragmentShader.id)
    }

    fun use() {
        if (activeShaderProgram != this) {
            glUseProgram(id)
            activeShaderProgram = this
        }
        uploadUniforms()
    }

    private fun uploadUniforms() {
        for ((name, value) in uniforms) {
            val location = glGetUniformLocation(id, name)
            when (value) {
                is Int -> glUniform1i(location, value)
                is Float -> glUniform1f(location, value)
                is Boolean -> glUniform1i(location, if (value) 1 else 0)
                is Vec2 -> glUniform2f(location, value.x, value.y)
                is Matrix4 -> glUniformMatrix4fv(location, true, value.toFloatArray())
            }
        }
        uniforms.clear()
    }

    fun setUniformInt(name: String, value: Int) {
        uniforms[name] = value
    }

    fun setUniformFloat(name: String, value: Float) {
        uniforms[name] = value
    }

    fun setUniformBool(name: String, value: Boolean) {
        uniforms[name] = value
    }

    fun setUniformVec2(name: String, value: Vec2) {
        uniforms[name] = value
    }

    fun setUniformMat4(name: String, value: Matrix4) {
        uniforms[name] = value
    }
}