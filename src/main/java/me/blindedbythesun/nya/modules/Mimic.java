package me.blindedbythesun.nya.modules;

import me.blindedbythesun.nya.Addon;
import me.blindedbythesun.nya.modules.notifications.NotificationType;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class Mimic extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<String> target = sgGeneral.add(new StringSetting.Builder()
        .name("Target Username")
        .description("The username of the player whose messages you want to mimic")
        .defaultValue("popbob")
        .build()
    );

    private final Setting<Boolean> greenText = sgGeneral.add(new BoolSetting.Builder()
        .name("Green Text")
        .description("Enables green text for mimicked messages by adding a > prefix")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> antiTrolling = sgGeneral.add(new BoolSetting.Builder()
        .name("Anti Troll")
        .description("Protect yourself from being trolled by replacing your name with the targets")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> privateMessageEnabled = sgGeneral.add(new BoolSetting.Builder()
        .name("Enable Private Message")
        .description("Enable sending a private message to the mimicked player")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> privateMessageContent = sgGeneral.add(new StringSetting.Builder()
        .name("Private Message Text")
        .description("The private message to send to the mimicked player")
        .defaultValue("nya")
        .visible(privateMessageEnabled::get)
        .build()
    );

    public Mimic() {
        super(Addon.CATEGORY, "mimic", "Repeats messages of a specified player");
    }

    @Override
    public void onActivate() {
        if(target.get().isEmpty()) {
            warning("Target name is empty, please set a player name.");
            if (Addon.notifications.isActive()) {
                Addon.notifications.addNotification("Invalid Config (Mimic)", "Target name is empty, please set a player name.", 4000, NotificationType.WARNING);
            }
            this.toggle();
            return;
        }

        if(!isPlayerOnline(target.get())) {
            String message = String.format("Your mimic target, %s, is not online");
            warning(message);
            if (Addon.notifications.isActive()) {
                Addon.notifications.addNotification("Mimic", message, 4000, NotificationType.WARNING);
            }
        }
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if(!(event.packet instanceof GameMessageS2CPacket packet)) return;

        String formattedMessage = packet.content().getString();
        String senderName = extractSenderName(formattedMessage);

        if(!senderName.equalsIgnoreCase(target.get())) return;

        String messageContent = extractMessageContent(formattedMessage);

        if(antiTrolling.get()) {
            messageContent = messageContent.replaceAll("(?i)" + mc.player.getName().getString(), target.get());
        }

        if(!messageContent.isEmpty()) {
            mc.player.networkHandler.sendChatMessage(greenText.get() ? ">" + messageContent : messageContent);

            if(privateMessageEnabled.get() && !privateMessageContent.get().isEmpty()) {
                mc.player.networkHandler.sendCommand("w " + target.get() + " " + privateMessageContent.get());
            }
        }
    }

    private String extractSenderName(String message) {
        if(message.startsWith("<") && message.contains(">")) {
            int endIndex = message.indexOf(">");
            return message.substring(1, endIndex);
        }
        return "";
    }

    private String extractMessageContent(String message) {
        if(message.startsWith("<") && message.contains(">")) {
            int startIndex = message.indexOf(">");
            return message.substring(startIndex + 2);
        }
        return "";
    }

    private boolean isPlayerOnline(String playerName) {
        return mc.getNetworkHandler().getPlayerList().stream().anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(playerName));
    }

}
