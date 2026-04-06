package me.formercanuck;

import me.formercanuck.entity.Entity;
import me.formercanuck.render.Camera;
import me.formercanuck.render.Renderer;
import me.formercanuck.world.World;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Game {

    private long window;

    private Renderer renderer;
    private World world;

    private Camera camera;

    public void run() {
        init();
        loop();
        glfwTerminate();
    }

    private void init() {
        glfwInit();

        window = glfwCreateWindow(1280, 720, "Excavate Or Die", 0, 0);
        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        double[] lastX = {400}, lastY = {300};
        boolean[] firstMouse = {true};

        glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {

            if (firstMouse[0]) {
                lastX[0] = xpos;
                lastY[0] = ypos;
                firstMouse[0] = false;
            }

            float xoffset = (float) (xpos - lastX[0]);
            float yoffset = (float) (lastY[0] - ypos); // reversed

            lastX[0] = xpos;
            lastY[0] = ypos;

            xoffset *= camera.sensitivity;
            yoffset *= camera.sensitivity;

            camera.yaw += xoffset;
            camera.pitch += yoffset;

            // clamp pitch
            if (camera.pitch > 89.0f) camera.pitch = 89.0f;
            if (camera.pitch < -89.0f) camera.pitch = -89.0f;
        });

        camera = new Camera();
        renderer = new Renderer(camera);
        world = new World();

        Entity cube = new Entity(1);
        cube.position.set(0, 0, 0);
        world.entities.put(1, cube);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {

            float deltaTime = 0.016f; // replace later with real delta

            float velocity = camera.speed * deltaTime;
            Vector3f front = camera.getFront();
            Vector3f right = new Vector3f(front).cross(0, 1, 0).normalize();

            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
                camera.position.add(new Vector3f(front).mul(velocity));

            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
                camera.position.sub(new Vector3f(front).mul(velocity));

            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
                camera.position.sub(new Vector3f(right).mul(velocity));

            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
                camera.position.add(new Vector3f(right).mul(velocity));

            // --- SIMULATE SERVER UPDATE ---
            for (Entity e : world.entities.values()) {
                e.rotation.y += 0.01f; // pretend server is rotating it
            }

            // --- RENDER ---
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderer.render(world);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
