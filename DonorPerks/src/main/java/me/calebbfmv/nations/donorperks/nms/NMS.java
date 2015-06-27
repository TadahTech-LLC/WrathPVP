package me.calebbfmv.nations.donorperks.nms;

import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class NMS {

    private static Field GOAL_FIELD = getField(PathfinderGoalSelector.class, "b");

    public static Field getField(Class<?> clazz, String field) {
        if (clazz == null) {
            return null;
        }
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
            f.setAccessible(true);
        } catch (Exception ignored) {

        }
        return f;
    }

    public static void clearGoals(PathfinderGoalSelector... goalSelectors) {
        if (GOAL_FIELD == null || goalSelectors == null) {
            return;
        }
        for (PathfinderGoalSelector selector : goalSelectors) {
            try {
                List<?> list = (List<?>) NMS.GOAL_FIELD.get(selector);
                list.clear();
            } catch (Exception ignored) {

            }
        }
    }


}
