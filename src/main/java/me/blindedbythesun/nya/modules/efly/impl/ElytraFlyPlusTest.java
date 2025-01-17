package me.blindedbythesun.nya.modules.efly.impl;

import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusMode;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusModes;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ElytraFlyPlusTest extends ElytraFlyPlusMode {


    public ElytraFlyPlusTest() {
        super(ElytraFlyPlusModes.Test);
    }



}
