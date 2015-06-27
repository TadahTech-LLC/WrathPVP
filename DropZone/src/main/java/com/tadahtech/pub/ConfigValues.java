package com.tadahtech.pub;

import com.tadahtech.pub.announcer.Announcement;
import com.tadahtech.pub.commands.DropZoneCommand;
import com.tadahtech.pub.shield.ShieldEffect;
import com.tadahtech.pub.shield.ShieldStructure;
import com.tadahtech.pub.shield.struct.BlockStructure;
import com.tadahtech.pub.shield.struct.ParticleStructure;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.tier.TierItem;
import com.tadahtech.pub.utils.ItemBuilder;
import com.tadahtech.pub.utils.Time;
import com.tadahtech.pub.utils.Utils;
import com.tadahtech.pub.utils.WrappedEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Timothy Andis
 */
public class ConfigValues {

    public static String PREFIX = ChatColor.GRAY.toString() + ChatColor.BOLD + "[" + ChatColor.DARK_AQUA + "DropZone" + ChatColor.GRAY + ChatColor.BOLD + "] ";

    protected final DropZone plugin = DropZone.getInstance();
    protected final File dir = new File(plugin.getDataFolder().getAbsolutePath());
    private FileConfiguration config;
    private ShieldStructure structure;
    private List<Location> dropLocations;
    private List<Tier> tiers;
    private Map<String, String> worldAliases;
    private final Random random = new Random();
    private String revealMessage;
    private String claimMessage;
    private String baseMessage;

    public ConfigValues() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        this.dropLocations = new ArrayList<>();
        this.worldAliases = new HashMap<>();
        this.tiers = new ArrayList<>();
        String struc = config.getString("structure");
        switch (struc) {
            case "BLOCK":
                this.structure = new BlockStructure();
                break;
            case "PARTICLE":
                this.structure = new ParticleStructure();
                break;
        }
        loadAliases();
        loadMessages();
        loadLocations();
        loadTiers();
    }

    public void loadAliases() {
        FileConfiguration config = config("worldNames");
        for (String s : config.getKeys(false)) {
            worldAliases.put(s, config.getString(s));
        }
    }

    public void loadTiers() {
        FileConfiguration config = config("tiers");
        ConfigurationSection section = config.getConfigurationSection("tiers");
        for (String t : section.getKeys(false)) {
            ConfigurationSection tier = section.getConfigurationSection(t);
            List<String> list = tier.getStringList("items");
            List<TierItem> itemStacks = new ArrayList<>();
            for (String s : list) {
                String[] str = s.split(" ");
                String type = str[0];
                if (type.contains(":")) {
                    String[] data = type.split(":");
                    String dType = data[0];
//                    String value = data[1];
                    switch (dType.toLowerCase()) {
                        case "armor": {
                            ItemStack[] items = new ItemStack[]{
                              read("helmet"),
                              read("chestplate"),
                              read("legs"),
                              read("boots")
                            };
                            List<ItemStack> temp = new ArrayList<>();
                            for (ItemStack itemStack : items) {
                                ItemBuilder builder = ItemBuilder.wrap(itemStack);
                                builder = read(str, builder);
                                temp.add(builder.build());
                            }
                            itemStacks.add(new TierItem(temp.toArray(new ItemStack[temp.size()])));
                            temp.clear();
                            break;
                        }
                        case "tools":
                            ItemStack[] items = new ItemStack[]{
                              read("axe"),
                              read("pick"),
                              read("spade"),
                            };
                            List<ItemStack> temp = new ArrayList<>();
                            for (ItemStack itemStack : items) {
                                ItemBuilder builder = ItemBuilder.wrap(itemStack);
                                builder = read(str, builder);
                                temp.add(builder.build());
                            }
                            itemStacks.add(new TierItem(temp.toArray(new ItemStack[temp.size()])));
                            temp.clear();
                            break;
                    }
                } else {
                    ItemStack itemStack = new ItemStack(Material.matchMaterial(type));
                    ItemBuilder builder = ItemBuilder.wrap(itemStack);
                    builder = read(str, builder);
                    itemStacks.add(new TierItem(builder.build()));
                }
            }
            int amount;
            String amountS = tier.getString("amount");
            try {
                amount = Integer.parseInt(amountS);
                this.tiers.add(new Tier(Integer.parseInt(t), amount, itemStacks));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private ItemStack read(String type) {
        switch (type.toLowerCase()) {
            case "helmet":
                return new ItemStack(Material.DIAMOND_HELMET);
            case "chestplate":
                return new ItemStack(Material.DIAMOND_CHESTPLATE);
            case "legs":
                return new ItemStack(Material.DIAMOND_LEGGINGS);
            case "boots":
                return new ItemStack(Material.DIAMOND_BOOTS);
            case "pick":
                return new ItemStack(Material.DIAMOND_PICKAXE);
            case "axe":
                return new ItemStack(Material.DIAMOND_AXE);
            case "spade":
                return new ItemStack(Material.DIAMOND_SPADE);

        }
        return null;
    }

    private ItemBuilder read(String[] str, ItemBuilder builder) {
        for (String opt : str[1].split("-")) {
            String[] values = opt.split(":");
            if (values.length != 2) {
                continue;
            }
            String option = values[0];
            String value = values[1];
            switch (option.toLowerCase()) {
                case "amount":
                    int amount;
                    try {
                        amount = Integer.parseInt(value);
                        builder.amount(amount);
                    } catch (NumberFormatException e) {
                        String[] split = value.split("-");
                        int min = Integer.parseInt(split[1]);
                        int max = Integer.parseInt(split[2]);
                        builder.amount(random.nextInt(max - min) + min);
                    }
                    break;
                case "name":
                    builder.name(value);
                    break;
                case "data":
                    byte data = Byte.parseByte(value);
                    builder.data(data);
                    break;
                case "lore":
                    String[] lr = value.split(",");
                    builder.lore(lr);
                    break;
                case "enchants":
                    if (value.equalsIgnoreCase("GLOW")) {
                        builder.glow(true);
                    } else {
                        List<WrappedEnchantment> enchants = new ArrayList<>();
                        String[] strings = value.split(",");
                        for (String e : strings) {
                            String[] enc = e.split("&");
                            enchants.add(new WrappedEnchantment(Utils.getFromString(enc[0]),
                              Integer.parseInt(enc[1])));
                        }
                        builder.enchant(enchants.toArray(new WrappedEnchantment[enchants.size()]));
                    }
                    break;
            }
        }
        return builder;
    }

    public void loadLocations() {
        FileConfiguration config = config("locations");
        List<String> locations = config.getStringList("locations");
        if (locations == null) {
            return;
        }
        this.dropLocations.addAll(locations.stream().map(Utils::locFromString).collect(Collectors.toList()));
    }

    public void loadMessages() {
        FileConfiguration config = config("messages");
        ConfigurationSection section = config.getConfigurationSection("drop");
        this.revealMessage = section.getString("revealMessage");
        this.claimMessage = config.getString("claimed");
        this.baseMessage = config.getString("baseNotRevealed");
        ShieldEffect.MESSAGE = config.getString("shieldsUp");
        DropZoneCommand.MESSAGE = config.getString("force");
        String base = section.getString("baseMessage");
        List<String> times = section.getStringList("times");
        for (String s : times) {
            boolean reveal = false;
            if (s.equalsIgnoreCase("0:30")) {
                reveal = true;
            }
            //Only hour
            if (!s.contains(":")) {
                int hour = Integer.parseInt(s);
                new Announcement(new Time(hour, 0, 0), base, reveal);
                continue;
            }
            String[] str = s.split(":");
            int len = str.length;
            switch (len) {
                case 1: {
                    int hour = Integer.parseInt(str[0]);
                    new Announcement(new Time(hour, 0, 0), base, reveal);
                    break;
                }
                case 2: {
                    int hours = Integer.parseInt(str[0]);
                    int minutes = Integer.parseInt(str[1]);
                    new Announcement(new Time(hours, minutes, 0), base, reveal);
                    break;
                }
                case 3: {
                    int hours = Integer.parseInt(str[0]);
                    int minutes = Integer.parseInt(str[1]);
                    int seconds = Integer.parseInt(str[2]);
                    new Announcement(new Time(hours, minutes, seconds), base, reveal);
                    break;
                }
            }
        }
    }

    public String getAlias(World world) {
        if (world == null || world.getName() == null) {
            return "?WORLD?";
        }
        return worldAliases.get(world.getName());
    }

    private FileConfiguration config(String file) {
        File fil = new File(dir, file + ".yml");
        if (!fil.exists()) {
            DropZone.getInstance().saveResource(fil.getName(), false);
        }
        return YamlConfiguration.loadConfiguration(fil);
    }

    public void saveLocations() {
        FileConfiguration config = config("locations");
        List<String> list = new ArrayList<>();
        dropLocations.stream().forEach(location -> list.add(Utils.locToString(location)));
        config.set("locations", list);
        File file = new File(dir, "locations.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShieldStructure getShieldStructure() {
        return structure;
    }

    public Location randomLocation() {
        return dropLocations.get(random.nextInt(dropLocations.size() - 1));
    }

    public Tier randomTier() {
        int next = random.nextInt(100) + 1;
        if (next < 40) {
            return tiers.get(0);
        }
        if (next >= 40 && next < 75) {
            return tiers.get(1);
        }
        if (next >= 25) {
            return tiers.get(2);
        }
        return tiers.get(random.nextInt(tiers.size()));
    }

    public boolean reload() {
        saveLocations();
        dropLocations.clear();
        tiers.clear();
        Tier.clear();
        loadLocations();
        loadAliases();
        loadTiers();
        loadMessages();
        String struc = config.getString("structure");
        switch (struc) {
            case "BLOCK":
                this.structure = new BlockStructure();
                break;
            case "PARTICLE":
                this.structure = new ParticleStructure();
                break;
        }
        return true;
    }

    public void addLocation(Block block) {
        this.dropLocations.add(block.getLocation());
    }

    public String getRevealMessage() {
        return revealMessage;
    }

    public String getClaimMessage() {
        return claimMessage;
    }

    public String getBaseMessage() {
        return baseMessage;
    }
}
