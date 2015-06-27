package com.tadahtech.wrathpvp.enchants;

import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.MultishotEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.block.BlazingTouchEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.block.HasteEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.block.InfusionEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.block.LocksmithEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.damage.LifestealEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.damage.ParalyzeEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.damage.PoisonEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.damage.WitherEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing.NetherSkinEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing.ReplenishEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing.VisionaryEnchant;
import com.tadahtech.wrathpvp.enchants.listener.BlockListener;
import com.tadahtech.wrathpvp.enchants.listener.DamageListener;
import com.tadahtech.wrathpvp.enchants.listener.FishListener;
import com.tadahtech.wrathpvp.enchants.listener.MenuListener;
import com.tadahtech.wrathpvp.enchants.menu.menus.InputMenu;
import com.tadahtech.wrathpvp.enchants.utils.GlowEnchant;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timothy Andis
 */
public class Enchants extends JavaPlugin {

	private static Enchants instance;
	private Map<String, IEnchant> enchants;
	public static int LAST_ID = 220;
	private Economy economy;


	public static Enchants getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) {
			System.out.println("NULL ECON...");
		} else {
			this.economy = rsp.getProvider();
		}
		this.saveDefaultConfig();
		doDefault();
		this.enchants = new HashMap<>();
		//BLOCK
		this.enchants.put(InfusionEnchant.INSTANCE.getName(), InfusionEnchant.INSTANCE);
		this.enchants.put(HasteEnchant.INSTANCE.getName(), HasteEnchant.INSTANCE);
		this.enchants.put(LocksmithEnchant.INSTANCE.getName(), LocksmithEnchant.INSTANCE);
		this.enchants.put(BlazingTouchEnchant.INSTANCE.getName(), BlazingTouchEnchant.INSTANCE);
		//DAMAGE
		this.enchants.put(LifestealEnchant.INSTANCE.getName(), LifestealEnchant.INSTANCE);
		this.enchants.put(ParalyzeEnchant.INSTANCE.getName(), ParalyzeEnchant.INSTANCE);
		this.enchants.put(PoisonEnchant.INSTANCE.getName(), PoisonEnchant.INSTANCE);
		this.enchants.put(WitherEnchant.INSTANCE.getName(), WitherEnchant.INSTANCE);
		//WEARING
		this.enchants.put(NetherSkinEnchant.INSTANCE.getName(), NetherSkinEnchant.INSTANCE);
		this.enchants.put(ReplenishEnchant.INSTANCE.getName(), ReplenishEnchant.INSTANCE);
		this.enchants.put(VisionaryEnchant.INSTANCE.getName(), VisionaryEnchant.INSTANCE);
		//BOW
		this.enchants.put(MultishotEnchant.INSTANCE.getName(), MultishotEnchant.INSTANCE);
		GlowEnchant.register();
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new DamageListener(), this);
		getServer().getPluginManager().registerEvents(new FishListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
	}

	private void doDefault() {
		File file = new File(getDataFolder(), "config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.getConfigurationSection("vanilla-costs") != null) {
			return;
		}
		for(Enchantment enchantment : Enchantment.values()) {
			if(enchantment instanceof IEnchant) {
				continue;
			}
			config.set("vanilla-costs." + enchantment.getName(), 10);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

	public int getCost(Enchantment enchantment) {
		return getConfig().getInt("vanilla-costs." + enchantment.getName());
	}

	@Override
	public void onDisable() {
		enchants.values().forEach(IEnchant::disable);
	}

	public Economy getEconomy() {
		return economy;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		new InputMenu().open((Player) sender);
		return true;
	}
}
