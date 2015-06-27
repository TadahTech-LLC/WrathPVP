package me.calebbfmv.nations.donorperks.perks;


import me.calebbfmv.nations.donorperks.perks.giant.Giant;
import me.calebbfmv.nations.donorperks.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class Perk {

    public static Archer ARCHER = new Archer(new ItemBuilder(new ItemStack(Material.BOW))
      .name(ChatColor.YELLOW + "Archers Bow of Mysticism")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Unleash your fiery wrath.",
        ChatColor.GRAY + "  - Blind your target, and set them ablaze.", " ",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "15 Seconds", " ")
      .build());

    public static Barbarian BARBARIAN = new Barbarian(new ItemBuilder(new ItemStack(Material.STONE_SWORD))
      .name(ChatColor.YELLOW + "Barbaric Sword")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Call upon your barbaric ancestors",
        ChatColor.GRAY + "  - Knockback players within " + ChatColor.GREEN + "10" + ChatColor.GRAY + " blocks",
        ChatColor.GRAY + "  - Deal 3 hearts of damage.", " ",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "15 Seconds")
      .build());

    public static BarbarianKing BARBARIAN_KING = new BarbarianKing(new ItemBuilder(new ItemStack(Material.DIAMOND_SWORD))
      .name(ChatColor.AQUA.toString() + ChatColor.BOLD + "Barbaric Scepter of Might")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Rule your Kingdom with the Might of Old!",
        ChatColor.GRAY + "  - Deal 8 additional hearts of damage",
        ChatColor.GRAY + "  - Adorne your enemies Armor as your own")
      .build());

    public static Dragon DRAGON = new Dragon(new ItemBuilder(new ItemStack(Material.FLINT_AND_STEEL))
      .name(ChatColor.GRAY.toString() + ChatColor.BOLD + "Dragon's Claw")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  The Power of Dragons",
        ChatColor.GRAY + "  - Spew fire in your path!",
        ChatColor.GRAY + "  - Become immune to all damage!",
        ChatColor.GRAY + "  - Deals an additional 3 hearts of damage",
        ChatColor.GRAY + "  - Fly around your enemies!", " ",
        ChatColor.GRAY + "Duration: " + ChatColor.RED + "7 seconds",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "15 Seconds", " ")
      .build());

    public static Golem GOLEM = new Golem(new ItemBuilder(new ItemStack(Material.SMOOTH_BRICK))
      .name(ChatColor.GRAY + "Giants Bolder")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Sing your Siren Song",
        ChatColor.GRAY + "  - All players within " + ChatColor.RED + "15" + ChatColor.GRAY + " blocks",
        ChatColor.GRAY + "  - will be drawn to you and pay for it.",
        ChatColor.GRAY + "  - They will be tossed into the wind",
        ChatColor.GRAY + "  - and damaged 5 hearts", " ",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "15 Seconds", " ")
      .build());

    public static Giant GIANT = new Giant(new ItemBuilder(new ItemStack(Material.IRON_AXE))
      .name(ChatColor.DARK_GRAY + "Giants's Club")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Harness the power of Metal Giants",
        ChatColor.GRAY + "  - Command 2 Iron Golems",
        ChatColor.GRAY + "  - Attacking any mob or player you attack", "",
        ChatColor.GRAY + "Duration: " + ChatColor.RED + "60 seconds",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "5 minutes")
      .build());

    public static Pekka PEKKA = new Pekka(new ItemBuilder(new ItemStack(Material.GOLD_SWORD))
      .name(ChatColor.DARK_GRAY + "Titanium Sword")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Equip the Armor of the PEKKA",
        ChatColor.GRAY + "  - Multiply your damage done by 10",
        ChatColor.GRAY + "  - Break your opponents armor (all or a piece)",
        ChatColor.GRAY + "  - Become invincible to all damage",
        ChatColor.GRAY + "  - while you block.")
      .build());

    public static Witch WITCH = new Witch(new ItemBuilder(new ItemStack(Material.SKULL_ITEM))
      .name(ChatColor.DARK_GRAY + "Witch's Skull")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Summon the Dead",
        ChatColor.GRAY + "  - An army of the dead will serve you",
        ChatColor.GRAY + "  - Attacking any mob or player you attack", "",
        ChatColor.GRAY + "Duration: " + ChatColor.RED + "60 seconds",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "5 minutes", " ")
      .build());

    public static Wizard WIZARD = new Wizard(new ItemBuilder(new ItemStack(Material.FIREWORK_CHARGE))
      .name(ChatColor.DARK_GRAY + "Wizard's Fireball")
      .lore(" ", ChatColor.GRAY + "Right Click to:", ChatColor.RED + "  Use the Mystic Powers of the Mage",
        ChatColor.GRAY + "  - Spew Fireballs in the direction you're looking", " ",
        ChatColor.GRAY + "Duration: " + ChatColor.RED + "7 seconds",
        ChatColor.GRAY + "Cooldown: " + ChatColor.RED + "15 Seconds", " ")
      .build());
}