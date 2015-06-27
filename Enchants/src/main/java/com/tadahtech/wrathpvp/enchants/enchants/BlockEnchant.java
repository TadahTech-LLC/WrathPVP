package com.tadahtech.wrathpvp.enchants.enchants;

import com.tadahtech.wrathpvp.enchants.Enchants;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

/**
 * Created by Timothy Andis
 */
public abstract class BlockEnchant extends Enchantment implements IEnchant {

	public BlockEnchant() {
		super(Enchants.LAST_ID++);
		register(this);
	}

	@Override
	public void onPlace(BlockPlaceEvent event, int level) {

	}

	@Override
	public void onHit(EntityDamageEvent event, int level) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, int level) {

	}

	@Override
	public void onFish(PlayerFishEvent event, int level) {

	}

	@Override
	public String getName() {
		return getClass().getSimpleName().replace("Enchant", "");
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantableTarget[] getEnchantableTarget() {
		return new EnchantableTarget[] {
		  EnchantableTarget.AXE, EnchantableTarget.PICKAXE, EnchantableTarget.SHOVEL
		};
	}

	@Override
	public Enchantment getEnchantment() {
		return this;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean canEnchantItem(ItemStack itemStack) {
		return false;
	}

	public void vanillaBreakMethods(Player player, Location block_location, boolean do_drop, boolean do_exp, boolean do_durability, int force_use_exp_amount) {
		try {
			CraftPlayer exception = (CraftPlayer)player;
			World nms_world = exception.getHandle().getWorld();
//			net.minecraft.server.v1_7_R4.ItemStack nms_itemstack = exception.getHandle().bF();
			net.minecraft.server.v1_7_R4.Block nms_world_block = nms_world.getType(block_location.getBlockX(), block_location.getBlockY(), block_location.getBlockZ());
			if(do_durability) {
				vanillaNMSToolDurabilityMethod(block_location.getBlock(), player);
			}

			if(do_exp && exception.getHandle().a(nms_world_block) && (!(Boolean) invokeBlockClassMethod(nms_world_block, net.minecraft.server.v1_7_R4.Block.class, "E") || !EnchantmentManager.hasSilkTouchEnchantment(exception.getHandle()))) {
				byte data = block_location.getBlock().getData();
				int bonusLevel = EnchantmentManager.getBonusBlockLootEnchantmentLevel(exception.getHandle());
				int exp_to_drop = force_use_exp_amount > 0?force_use_exp_amount:nms_world_block.getExpDrop(nms_world, data, bonusLevel);
				dropExperience(nms_world, block_location.getBlockX(), block_location.getBlockY(), block_location.getBlockZ(), exp_to_drop);
			}

			if(do_drop && exception.getHandle().a(nms_world_block)) {
				nms_world_block.a(nms_world, exception.getHandle(), block_location.getBlockX(), block_location.getBlockY(), block_location.getBlockZ(), nms_world.getData(block_location.getBlockX(), block_location.getBlockY(), block_location.getBlockZ()));
			}
		} catch (Exception var13) {
			var13.printStackTrace();
		}

	}

	protected Object invokeBlockClassMethod(net.minecraft.server.v1_7_R4.Block block, Class<net.minecraft.server.v1_7_R4.Block> using_class, String method_name) throws Exception {
		Method method = using_class.getDeclaredMethod(method_name);
		method.setAccessible(true);
		return method.invoke(block);
	}

	protected void dropExperience(World world, int x, int y, int z, int i) {
		if(!world.isStatic) {
			while(i > 0) {
				int j = EntityExperienceOrb.getOrbValue(i);
				i -= j;
				world.addEntity(new EntityExperienceOrb(world, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, j));
			}
		}

	}

	protected void vanillaNMSToolDurabilityMethod(Block block, Player player) {
		CraftPlayer craft_player = (CraftPlayer)player;
		EntityPlayer nms_player = craft_player.getHandle();
		WorldServer nms_world = ((CraftWorld)craft_player.getWorld()).getHandle();
		net.minecraft.server.v1_7_R4.ItemStack itemstack1 = nms_player.bF();
		if(itemstack1 != null) {
			itemstack1.a(nms_world, nms_world.getType(block.getX(), block.getY(), block.getZ()), block.getX(), block.getY(), block.getZ(), craft_player.getHandle());
			if(itemstack1.count == 0) {
				nms_player.bG();
			}
		}

	}
}
