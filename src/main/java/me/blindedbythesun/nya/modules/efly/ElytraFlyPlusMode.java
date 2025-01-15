package me.blindedbythesun.nya.modules.efly;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;

public class ElytraFlyPlusMode {

    protected final MinecraftClient mc;
    protected final ElytraFlyPlus settings;
    private final ElytraFlyPlusModes type;

    public void onPreTick(TickEvent.Pre event) {}
    public void onPostTick(TickEvent.Post event) {}
    public void onSendPacket(PacketEvent.Send event) {}

    public ElytraFlyPlusMode(ElytraFlyPlusModes type) {
        this.settings = Modules.get().get(ElytraFlyPlus.class);
        this.mc = MinecraftClient.getInstance();
        this.type = type;
    }

    public String getHudString() {
        return type.name();
    }

}
