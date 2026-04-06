package me.formercanuck.render;

import me.formercanuck.world.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    private int vao;
    private int vertexCount;

    public static Mesh createCube(Set<Direction> faces) {

        List<Float> verts = new ArrayList<>();

        for (Direction face : faces) {
            addFace(verts, face);
        }

        float[] vertices = new float[verts.size()];
        for (int i = 0; i < verts.size(); i++) {
            vertices[i] = verts.get(i);
        }

        Mesh mesh = new Mesh();
        mesh.vertexCount = vertices.length / 5;

        mesh.vao = glGenVertexArrays();
        int vbo = glGenBuffers();

        glBindVertexArray(mesh.vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        return mesh;
    }

    private static void addFace(List<Float> v, Direction d) {

        switch (d) {

            case NORTH -> addQuad(v,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f
            );

            case SOUTH -> addQuad(v,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f
            );

            case EAST -> addQuad(v,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f
            );

            case WEST -> addQuad(v,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f
            );

            case UP -> addQuad(v,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f
            );

            case DOWN -> addQuad(v,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f
            );
        }
    }

    private static void addQuad(List<Float> v,
                                float x0, float y0, float z0,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float x3, float y3, float z3) {

        // Triangle 1
        addVertex(v, x0, y0, z0, 0, 0);
        addVertex(v, x1, y1, z1, 1, 0);
        addVertex(v, x2, y2, z2, 1, 1);

        // Triangle 2
        addVertex(v, x2, y2, z2, 1, 1);
        addVertex(v, x3, y3, z3, 0, 1);
        addVertex(v, x0, y0, z0, 0, 0);
    }

    private static void addVertex(List<Float> v, float x, float y, float z, float u, float t) {
        v.add(x);
        v.add(y);
        v.add(z);
        v.add(u);
        v.add(t);
    }

    public void render() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    }
}