package me.blindedbythesun.nya.modules;

import me.blindedbythesun.nya.Addon;
import me.blindedbythesun.nya.modules.notifications.NotificationType;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AutoMeow extends Module {


    public long lastMeowTime, meowDelay;
    public List<String> meows = Arrays.asList("meow", "mrrrp meow");
    public List<String> suffixes = Arrays.asList("~", "~ ;3", "~ :3", " ;3", " :3", " >~<", " >w<", "~ >w<", " >x<");
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private Random random = new Random();

    public final Setting<Double> delayMinS = sgGeneral.add(new DoubleSetting.Builder()
        .name("min-delay")
        .description("How many seconds before meowing.")
        .defaultValue(60.0d)
        .min(5.0d)
        .sliderMax(60.0d*10)
        .build()
    );

    public final Setting<Double> delayMaxS = sgGeneral.add(new DoubleSetting.Builder()
        .name("max-delay")
        .description("How many seconds before meowing.")
        .defaultValue(60.0d)
        .min(5.0d)
        .sliderMax(60.0d*10)
        .build()
    );

    public void generateDelay() {
        if(delayMaxS.get().equals(delayMinS.get())) {
            meowDelay = (long)(delayMaxS.get() * 1000);
        }else {
            // you fucking idiot min is above max
            if(delayMinS.get() > delayMaxS.get()) {
                if (Addon.notifications.isActive()) {
                    Addon.notifications.addNotification("Invalid Config (Auto Meow)", "Max Delay must be above Min Delay", 4000, NotificationType.ERROR);
                }
                ChatUtils.sendMsg(Text.of("Auto Meow: Max Delay must be above Min Delay"));
                this.toggle();
                meowDelay = 1000;
                return;
            }
            meowDelay = (long) (((Math.random() * (delayMaxS.get() - delayMinS.get())) + delayMinS.get()) * 1000);
        }
    }

    public AutoMeow() {
        super(Addon.CATEGORY, "auto-meow", "fag maxxing");
    }

    @Override
    public void onActivate() {
        super.onActivate();
        lastMeowTime = System.currentTimeMillis();
        generateDelay();
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if(System.currentTimeMillis() >= lastMeowTime + meowDelay) {
            ChatUtils.sendPlayerMsg(meows.get(random.nextInt(meows.size())) + suffixes.get(random.nextInt(suffixes.size())));
            generateDelay();
            lastMeowTime = System.currentTimeMillis();
        }
    }

}
