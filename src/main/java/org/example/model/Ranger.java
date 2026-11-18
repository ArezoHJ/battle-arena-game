package org.example.model;

public final class Ranger extends Character {
    public Ranger() {
        super(105, 6, 16);
    }

    @Override
    public String getType() {
        return "Ranger";
    }
}
