package creakGL

val drawer = VertexArray(VertexBuffer(BufferUsage.DYNAMIC), intArrayOf(2, 4))

fun drawLine(
    start: Vec2, end: Vec2, color: Color
) {
    val vertices = floatArrayOf(
        start.x, start.y, color.r, color.g, color.b, color.a,
        end.x, end.y, color.r, color.g, color.b, color.a
    )
    drawer.vertexBuffer.setData(vertices)
    drawer.draw(Primitives.LINES, 2)
}