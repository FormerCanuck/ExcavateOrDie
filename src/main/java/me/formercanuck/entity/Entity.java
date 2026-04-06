package me.formercanuck.entity;

import org.joml.Vector3f;

public class Entity {
    public int id;

    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();

    public Entity(int id) {
        this.id = id;
    }
}
