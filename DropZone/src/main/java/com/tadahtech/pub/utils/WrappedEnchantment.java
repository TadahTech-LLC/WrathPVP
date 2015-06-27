package com.tadahtech.pub.utils;

import org.bukkit.enchantments.Enchantment;

/**
 * @author Timothy Andis
 */
public class WrappedEnchantment {

    private Enchantment enchantment;
    private int level;
    private boolean override;

    /**
     * Wrap an enchantment
     * @param enchantment The enchantment
     * @param level The level
     * @param override Overriding boolean
     */
    public WrappedEnchantment(Enchantment enchantment, int level, boolean override){
        this.enchantment = enchantment;
        this.level = level;
        this.override = override;
    }

    /**
     * Wrap an enchantment with a false overriding value
     * @param enchantment The enchantment
     * @param level The level
     */
    public WrappedEnchantment(Enchantment enchantment, int level){
        this(enchantment, level, true);
    }

    /**
     * Wrap an enchantment with level 1 and false value of overriding
     * @param enchantment The enchantment
     */
    public WrappedEnchantment(Enchantment enchantment){
        this(enchantment, 1, true);
    }

    /**
     * Wrap an enchantment with level 1
     * @param enchantment The enchantment
     * @param override Overriding boolean
     */
    public WrappedEnchantment(Enchantment enchantment, boolean override){
        this(enchantment, 1, override);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

}
