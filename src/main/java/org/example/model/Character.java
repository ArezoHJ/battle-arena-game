package org.example.model;

public abstract class Character implements SpecialAbility {
    private final int maxHP;
    private final int minimumDamage;
    private final int maximumDamage;
    private int hp;
    private boolean defending;

protected Character(int maxHP, int minimumDamage, int maximumDamage) {
        if (maxHP <= 0 || minimumDamage < 0 || maximumDamage < minimumDamage) {
            throw new IllegalArgumentException("Invalid character values.");
        }
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.minimumDamage = minimumDamage;
        this.maximumDamage = maximumDamage;
    }

    public abstract String getType();

    public final int getHp() {
        return hp;
    }

    public final int getMaxHP() {
        return maxHP;
    }

    public final boolean isAlive() {
        return hp > 0;
    }

    public final int attack(Character opponent) {
        //
    }

    public final int receiveDamage(int damage) {
        //
    }

    protected final int heal(int amount) {
        //
    }

    protected final void prepareDefend() {
        defending = true;
    }
}
