package me.blindedbythesun.nya.modules;

import me.blindedbythesun.nya.Addon;
import me.blindedbythesun.nya.modules.notifications.NotificationType;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.List;
import java.util.Random;

public class Suffix extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    Random rand = new Random();

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("suffixes")
        .description("Strings to append to your messages")
        .defaultValue(List.of("nya~"))
        .build()
    );

    private final Setting<Boolean> separator = sgGeneral.add(new BoolSetting.Builder()
        .name("separator")
        .description("Separates your message and suffix with ⏐")
        .defaultValue(true)
        .build()
    );

    public Suffix() {
        super(Addon.CATEGORY, "suffix", "Appends a suffix");
    }

    @Override
    public void onActivate() {
        if(messages.get().isEmpty()) {
            String message = String.format("You have no suffixes");
            warning(message);
            if (Addon.notifications.isActive()) {
                Addon.notifications.addNotification("Suffix", message, 4000, NotificationType.WARNING);
            }
        }
    }

    @EventHandler
    private void onMessageSend(SendMessageEvent event) {
        if(!messages.get().isEmpty()) {
            String suffix = messages.get().get(rand.nextInt(messages.get().size()));
            event.message += separator.get() ? " ⏐ " + suffix : " " + suffix;
        }
    }

}
