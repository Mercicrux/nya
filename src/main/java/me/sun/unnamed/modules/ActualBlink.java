package me.sun.unnamed.modules;

import me.sun.unnamed.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

public class ActualBlink extends Module {

    ArrayList<Packet> packets = new ArrayList<>();

    public ActualBlink() {
        super(Addon.CATEGORY, "actual-blink", "Blink but it blinks all packets");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        packets.add(event.packet);
        event.cancel();
    }

    @Override
    public void onDeactivate() {
        for(Packet p : packets) {
            mc.getNetworkHandler().sendPacket(p);
        }
        packets.clear();
    }
}
