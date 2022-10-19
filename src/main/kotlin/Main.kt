import creakGL.*

val window = Window.create(1280, 720, vSyncEnabled = false)
fun main() {

    val vertices = floatArrayOf(
        -1f, -1f, 1f, 0f, 0f, 1f,
        1f, -1f, 0f, 1f, 0f, 1f,
        1f, 1f, 0f, 0f, 1f, 1f,
        -1f, 1f, 1f, 1f, 0f, 1f
    )
    val indices = intArrayOf(
        0, 1, 2,
        0, 2, 3
    )
    val vertexBuffer = VertexBuffer(BufferUsage.STATIC, vertices)
    val vertexArray = VertexArray(vertexBuffer, intArrayOf(2, 4))
    vertexArray.indexBuffer = IndexBuffer(BufferUsage.STATIC, indices)

    val shader = ShaderProgram(
        VertexShader.load("src\\main\\resources\\shaders\\vertex.glsl"),
        FragmentShader.load("src\\main\\resources\\shaders\\fragment.glsl")
    )

    shader.compileAndLink()
    shader.use()

    window.run {
        repeat(100) {
            vertexArray.drawIndexed(Primitives.TRIANGLES, 6)
        }
    }
}