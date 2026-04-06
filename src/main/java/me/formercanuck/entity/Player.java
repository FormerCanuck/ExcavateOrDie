package me.formercanuck.entity;

public class Player extends Entity {

    public boolean isLocal;

    public Player(int id, boolean isLocal) {
        super(id);
        this.isLocal = isLocal;
    }
}
