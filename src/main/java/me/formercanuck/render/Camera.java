package me.formercanuck.render;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public Vector3f position = new Vector3f(0, 0, 3);

    public float yaw = -90f;   // looking toward -Z
    public float pitch = 0f;

    public float speed = 5f;
    public float sensitivity = 0.1f;

    public Matrix4f getViewMatrix() {
        Vector3f front = getFront();

        return new Matrix4f()
                .lookAt(
                        position,
                        new Vector3f(position).add(front),
                        new Vector3f(0, 1, 0)
                );
    }

    public Vector3f getFront() {
        Vector3f front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        return front.normalize();
    }
}