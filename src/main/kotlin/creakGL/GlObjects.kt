package creakGL

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

val glObjects = mutableListOf<GLObject>()

abstract class GLObject {
    fun queueGenerate() {
        if (isGLInit)
            generate()
        else
            glObjects.add(this)
    }
    abstract fun generate()
}

fun generateGLObjects() {
    for (glObject in glObjects)
        glObject.generate()
}

var boundVertexBuffer: VertexBuffer? = null
var boundVertexArray: VertexArray? = null

class VertexBuffer(val usage: BufferUsage, val size: Long = 100000L * Float.SIZE_BYTES): GLObject() {
    private var id = 0
    constructor(usage: BufferUsage, data: FloatArray): this(usage, data.size.toLong() * Float.SIZE_BYTES) {
        setData(data)
    }
    init {
        queueGenerate()
    }
    override fun generate() {
        id = glGenBuffers()
        bind()
        glBufferData(GL_ARRAY_BUFFER, size, usage.id)
    }

    fun bind() {
        if (boundVertexBuffer != this) {
            glBindBuffer(GL_ARRAY_BUFFER, id)
            boundVertexBuffer = this
        }
    }

    fun setData(data: FloatArray, offset: Long = 0) {
        bind()
        glBufferSubData(GL_ARRAY_BUFFER, offset, data)
    }
}

class IndexBuffer(val usage: BufferUsage, val size: Long): GLObject() {
    private var id = 0
    constructor(usage: BufferUsage, data: IntArray): this(usage, data.size.toLong() * Float.SIZE_BYTES) {
        setData(data)
    }
    init {
        queueGenerate()
    }
    override fun generate() {
        id = glGenBuffers()
        bind()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, size, usage.id)
    }

    fun bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
    }

    fun setData(data: IntArray, offset: Long = 0) {
        bind()
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data)
    }
}

enum class BufferUsage(val id: Int) {
    STATIC(GL_STATIC_DRAW), DYNAMIC(GL_DYNAMIC_DRAW), STREAM(GL_STREAM_DRAW)
}

class VertexArray(val vertexBuffer: VertexBuffer, val vertexAttributeSizes: IntArray): GLObject() {
    private var id = 0
    init {
        queueGenerate()
    }
    override fun generate() {
        id = glGenVertexArrays()
        glBindVertexArray(id)
        setAttributePointers()
    }

    fun bind() {
        if (boundVertexArray != this) {
            glBindVertexArray(id)
            boundVertexArray = this
        }
    }

    var indexBuffer: IndexBuffer? = null
        set(value) {
            bind()
            value?.bind()
            field = value
        }
    private fun setAttributePointers() {
        vertexBuffer.bind()
        var pointer = 0L
        for (i in vertexAttributeSizes.indices) {
            glVertexAttribPointer(
                i,
                vertexAttributeSizes[i],
                GL_FLOAT,
                false,
                vertexAttributeSizes.sum() * Float.SIZE_BYTES,
                pointer * Float.SIZE_BYTES
            )
            glEnableVertexAttribArray(i)
            pointer += vertexAttributeSizes[i]
        }
    }
    fun draw(primitive: Primitives, count: Int, start: Int = 0) {
        bind()
        glDrawArrays(primitive.id, start, count)
    }

    fun drawIndexed(primitive: Primitives, count: Int) {
        bind()
        glDrawElements(primitive.id, count, GL_UNSIGNED_INT, 0)
    }
}

enum class Primitives(val id: Int) {
    TRIANGLES(GL_TRIANGLES), TRIANGLE_STRIP(GL_TRIANGLE_STRIP), TRIANGLE_FAN(GL_TRIANGLE_FAN),
    LINES(GL_LINES), LINE_STRIP(GL_LINE_STRIP), LINE_LOOP(GL_LINE_LOOP),
    POINTS(GL_POINTS)
}