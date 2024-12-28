package me.sun.unnamed.modules;

import me.sun.unnamed.Addon;
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
    public List<String> meows = Arrays.asList("meow", "mrrow", "nyaa~", "meow :3", "meow ;3", "mewo >w<", "mrrrp nyaa :3", "meow >w<", "mrrp o//w//o");
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private Random random = new Random();

    public final Setting<Double> delayMinS = sgGeneral.add(new DoubleSetting.Builder()
        .name("Min Delay")
        .description("How many seconds before meowing.")
        .defaultValue(60.0d)
        .min(5.0d)
        .sliderMax(60.0d*6)
        .build()
    );

    public final Setting<Double> delayMaxS = sgGeneral.add(new DoubleSetting.Builder()
        .name("Max Delay")
        .description("How many seconds before meowing.")
        .defaultValue(60.0d)
        .min(5.0d)
        .sliderMax(60.0d*6)
        .build()
    );

    public void generateDelay() {
        if(delayMaxS.get().equals(delayMinS.get())) {
            meowDelay = (long)(delayMaxS.get() * 1000);
        }else {
            // you fucking idiot min is above max
            if(delayMinS.get() > delayMaxS.get()) {
                this.toggle();
                meowDelay = 1000;
                return;
            }
            ChatUtils.sendMsg(Text.of("Meow delay max must be above min"));
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
            ChatUtils.sendPlayerMsg(meows.get(random.nextInt(meows.size())));
            generateDelay();
            lastMeowTime = System.currentTimeMillis();
        }
    }

}
