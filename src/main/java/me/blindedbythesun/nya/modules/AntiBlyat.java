package me.blindedbythesun.nya.modules;

import me.blindedbythesun.nya.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

public class AntiBlyat extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<FilterMode> filterMode = sgGeneral.add(new EnumSetting.Builder<FilterMode>()
        .name("filter-mode")
        .description("How cyrillic words in chat are filtered")
        .defaultValue(FilterMode.HideMessage)
        .build()
    );

    public AntiBlyat() {
        super(Addon.CATEGORY, "anti-blyat", "Filters chat messages with cyrillic characters");
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event) {
        if(!(event.packet instanceof GameMessageS2CPacket packet)) return;

        String message = packet.content().getString();

        if(containsCyrillic(message)) {
            switch (filterMode.get()) {
                case HideMessage:
                    event.cancel();
                    break;
                case HideWords:
                    event.cancel();
                    String filteredMessage = removeCyrillicCharacters(message);
                    if(!filteredMessage.isBlank()) { // isBlank is superior to isEmpty here as it prevents a message of spaces being sent
                        mc.player.sendMessage(Text.literal(filteredMessage), false);
                    }
                    break;
            }
        }
    }

    private boolean containsCyrillic(String text) {
        return text.matches(".*[\\u0400-\\u04FF]+.*");
    }

    private String removeCyrillicCharacters(String text) {
        return text.replaceAll("[\\u0400-\\u04FF]", "");
    }

    public enum FilterMode {
        HideMessage,
        HideWords
    }
}
