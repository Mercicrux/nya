package me.blindedbythesun.nya.modules.efly.impl;

import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusMode;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusModes;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class ElytraFlyPlusPacket extends ElytraFlyPlusMode {

    public ElytraFlyPlusPacket() {
        super(ElytraFlyPlusModes.Packet);
    }

    public void onPreTick(TickEvent.Pre event) {
        if(!mc.player.isOnGround() && !mc.player.isSubmergedInWater() && mc.player.getVelocity().y <= 0) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            float movementSpeed = (float) Math.sqrt(Math.pow(mc.player.getVelocity().x, 2) + Math.pow(mc.player.getVelocity().z, 2));
            if(movementSpeed < settings.maxSpeedPacket.get()/20d) {
                double speed = settings.accelerationPacket.get();
                Vec3d vec3d = mc.player.getRotationVector();
                mc.player.setVelocity(mc.player.getVelocity().x + (vec3d.x*speed), -0.05 / Math.max(1,movementSpeed), mc.player.getVelocity().z + (vec3d.z*speed));
            }
            if(movementSpeed > 0.4) {
                float pitch = (float) -9.175d / movementSpeed;
                mc.player.setPitch(pitch);
            }
        }
    }

    public void onPostTick(TickEvent.Post event) {

    }

    public void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket) {
            ((PlayerMoveC2SPacketAccessor) event.packet).setOnGround(true);
        }
    }

}
