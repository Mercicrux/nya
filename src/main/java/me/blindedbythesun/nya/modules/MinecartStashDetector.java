package me.blindedbythesun.nya.modules;

import me.blindedbythesun.nya.Addon;
import me.blindedbythesun.nya.modules.notifications.NotificationType;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class MinecartStashDetector extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Integer> minMinecarts = sgGeneral.add(new IntSetting.Builder()
        .name("minimum-minecarts")
        .description("The minimum number of chest minecarts stacked on one block to trigger a notification")
        .defaultValue(3)
        .min(1)
        .max(20)
        .sliderMin(1)
        .sliderMax(1)
        .build()
    );

    private final HashMap<BlockPos, Long> detectedStashes = new HashMap<>();

    public MinecartStashDetector() {
        super(Addon.CATEGORY, "minecart-stash-detector", "Detects chest minecart stashes");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if(mc.world == null || mc.player == null) return;

        Map<BlockPos, Integer> minecartCounts = new HashMap<>();

        for(Entity entity : mc.world.getEntities()) {
            if(entity instanceof ChestMinecartEntity) {
                BlockPos pos = entity.getBlockPos();
                minecartCounts.put(pos, minecartCounts.getOrDefault(pos, 0) + 1);
            }
        }

        for(Map.Entry<BlockPos, Integer> entry : minecartCounts.entrySet()) {
            BlockPos pos = entry.getKey();
            int count = entry.getValue();

            if(count >= minMinecarts.get() && !detectedStashes.containsKey(pos)) {
                notifyPlayer(pos, count);
                detectedStashes.put(pos, System.currentTimeMillis());
            }
        }

        detectedStashes.entrySet().removeIf(entry ->
            System.currentTimeMillis() - entry.getValue() >= 10000
        );
    }

    private void notifyPlayer(BlockPos pos, int count) {
        String message = String.format("Found %d stacked chest minecarts at %d, %d, %d", count, pos.getX(), pos.getY(), pos.getZ());
        Addon.notifications.addNotification(message, 5000, NotificationType.WARNING);
    }

}
