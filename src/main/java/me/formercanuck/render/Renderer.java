package me.formercanuck.render;

import me.formercanuck.entity.Entity;
import me.formercanuck.world.Direction;
import me.formercanuck.world.World;
import org.joml.Matrix4f;

import java.util.EnumSet;

public class Renderer {

    private Mesh cubeMesh;
    private Shader shader;
    private Texture texture;

    private Matrix4f projection;

    private Camera camera;

    public Renderer(Camera camera) {
        this.camera = camera;
        cubeMesh = Mesh.createCube(EnumSet.allOf(Direction.class));
        shader = new Shader("src/main/resources/shaders/vertex.glsl",
                "src/main/resources/shaders/fragment.glsl");
        texture = new Texture("src/main/resources/textures/crate.png");

        projection = new Matrix4f()
                .perspective((float) Math.toRadians(70), 1280f / 720f, 0.1f, 100f);
    }

    public void render(World world) {
        shader.use();
        texture.bind();

        for (Entity e : world.entities.values()) {
            Matrix4f model = new Matrix4f()
                    .translate(e.position)
                    .rotateXYZ(e.rotation.x, e.rotation.y, e.rotation.z);

            Matrix4f mvp = new Matrix4f(projection).mul(camera.getViewMatrix()).mul(model);

            shader.setMat4("mvp", mvp);

            cubeMesh.render();
        }
    }
}