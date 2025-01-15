package me.blindedbythesun.nya.modules.efly.impl;

import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusMode;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusModes;
import meteordevelopment.meteorclient.events.world.TickEvent;
import net.minecraft.util.math.Vec3d;

public class ElytraFlyPlusBoost extends ElytraFlyPlusMode {

    public ElytraFlyPlusBoost() {
        super(ElytraFlyPlusModes.Boost);
    }

    public void onPreTick(TickEvent.Pre event) {
        if(mc.player.isFallFlying()) {
            float movementSpeed = (float) Math.sqrt(Math.pow(mc.player.getVelocity().x, 2) + Math.pow(mc.player.getVelocity().z, 2));
            if(movementSpeed < settings.maxSpeedBoost.get()/20d) {
                double speed = settings.accelerationBoost.get();
                if(mc.player.getPitch() < 0) speed -= Math.min(settings.accelerationBoost.get(), Math.abs(mc.player.getPitch())/900);
                if(mc.options.forwardKey.isPressed() || mc.options.jumpKey.isPressed()) {
                    Vec3d vec3d = mc.player.getRotationVector();
                    mc.player.setVelocity(mc.player.getVelocity().x + (vec3d.x*speed), mc.player.getVelocity().y, mc.player.getVelocity().z + (vec3d.z*speed));
                }
            }
        }
    }

}
