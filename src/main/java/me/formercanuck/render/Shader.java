package me.formercanuck.render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public int id;

    public Shader(String vPath, String fPath) {
        try {
            String vSrc = Files.readString(Path.of(vPath));
            String fSrc = Files.readString(Path.of(fPath));

            int v = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(v, vSrc);
            glCompileShader(v);

            int f = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(f, fSrc);
            glCompileShader(f);

            id = glCreateProgram();
            glAttachShader(id, v);
            glAttachShader(id, f);
            glLinkProgram(id);

            glDeleteShader(v);
            glDeleteShader(f);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void use() {
        glUseProgram(id);
    }

    public void setMat4(String name, Matrix4f mat) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        mat.get(fb);
        glUniformMatrix4fv(glGetUniformLocation(id, name), false, fb);
    }
}
