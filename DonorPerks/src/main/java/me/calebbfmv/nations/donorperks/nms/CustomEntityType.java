package me.calebbfmv.nations.donorperks.nms;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Timothy Andis
 */
public enum CustomEntityType {

    SKELETON("Skeleton", 51, CSkeleton.class),
    IRON_GOLEM("VillagerGolem", 99, SIronGolem.class);

    private CustomEntityType(String name, int id, Class<? extends EntityInsentient> custom) {
        addToMaps(custom, name, id);
    }

    public Entity spawnEntity(Location loc) {
        World world = ((CraftWorld) loc.getWorld()).getHandle();
        Entity entity;
        switch (this) {
            case IRON_GOLEM:
                entity = new SIronGolem(world);
                break;
            case SKELETON:
                entity = new CSkeleton(world);
                break;
            default:
                return null;
        }
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    private static void addToMaps(Class clazz, String name, int id) {
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, id);
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }
}
