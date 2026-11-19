package org.example.model;

import java.util.concurrent.ThreadLocalRandom;

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
        int damage = ThreadLocalRandom.current().nextInt(minimumDamage, maximumDamage + 1);
        return opponent.receiveDamage(damage);
    }

    public final int receiveDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        int effectiveDamage = defending ? damage / 2 : damage;
        defending = false;
        int actualDamage = Math.min(hp, effectiveDamage);
        hp -= actualDamage;
        return actualDamage;
    }

    protected final int heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Healing cannot be negative.");
        }
        int actualHealing = Math.min(amount, maxHP - hp);
        hp += actualHealing;
        return actualHealing;
    }

    protected final void prepareDefend() {
        defending = true;
    }
}
