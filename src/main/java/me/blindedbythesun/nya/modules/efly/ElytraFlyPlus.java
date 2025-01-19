package me.blindedbythesun.nya.modules.efly;

import me.blindedbythesun.nya.Addon;
import me.blindedbythesun.nya.modules.efly.impl.*;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class ElytraFlyPlus extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgSimple = this.settings.createGroup("Simple");
    private final SettingGroup sgSemiControl = this.settings.createGroup("Semi Control");
    private final SettingGroup sgPacket = this.settings.createGroup("Packet");
    private final SettingGroup sgBoost = this.settings.createGroup("Boost");

    public final Setting<ElytraFlyPlusModes> flightMode = sgGeneral.add(new EnumSetting.Builder<ElytraFlyPlusModes>()
        .name("mode")
        .description("The method of applying speed.")
        .defaultValue(ElytraFlyPlusModes.Simple)
        .onModuleActivated(flightMode -> onModeChanged(flightMode.get()))
        .onChanged(this::onModeChanged)
        .build()
    );

    public ElytraFlyPlusMode currentMode;

    private void onModeChanged(ElytraFlyPlusModes mode) {
        switch (mode) {
            case Boost -> currentMode = new ElytraFlyPlusBoost();
            case Simple -> currentMode = new ElytraFlyPlusSimple();
            case SemiControl -> currentMode = new ElytraFlyPlusSemiControl();
            case Packet -> currentMode = new ElytraFlyPlusPacket();
            case Test -> currentMode = new ElytraFlyPlusTest();
        }
    }

    public final Setting<Double> verticalAngle = sgSimple.add(new DoubleSetting.Builder()
        .name("Vertical Angle")
        .description("How much to affect aim when going vertically.")
        .defaultValue(20.0d)
        .min(10.0d)
        .sliderMax(30.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Simple)
        .build()
    );

    public final Setting<Double> verticalSpeed = sgSimple.add(new DoubleSetting.Builder()
        .name("Vertical Speed")
        .description("How much to affect aim when going vertically.")
        .defaultValue(0.015d)
        .range(0.005d, 0.05d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Simple)
        .build()
    );

    public final Setting<Double> horizontalAcceleration = sgSimple.add(new DoubleSetting.Builder()
        .name("Horizontal Acceleration")
        .description("The speed of acceleration.")
        .defaultValue(0.035d)
        .range(0.01d, 0.075d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Simple)
        .build()
    );

    public final Setting<Double> maxSpeedSimple = sgSimple.add(new DoubleSetting.Builder()
        .name("Max Speed")
        .description("Max BPS.")
        .defaultValue(48.0d)
        .min(10.0d)
        .sliderMax(100.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Simple)
        .build()
    );

    public final Setting<Double> verticalAngleSemiControl = sgSemiControl.add(new DoubleSetting.Builder()
        .name("Vertical Angle")
        .description("How much to affect aim when going vertically.")
        .defaultValue(20.0d)
        .min(10.0d)
        .sliderMax(30.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.SemiControl)
        .build()
    );

    public final Setting<Double> verticalSpeedSemiControl = sgSemiControl.add(new DoubleSetting.Builder()
        .name("Vertical Speed")
        .description("How much to affect aim when going vertically.")
        .defaultValue(0.015d)
        .range(0.005d, 0.05d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.SemiControl)
        .build()
    );

    public final Setting<Double> horizontalAccelerationSemiControl = sgSemiControl.add(new DoubleSetting.Builder()
        .name("Horizontal Acceleration")
        .description("The speed of acceleration.")
        .defaultValue(0.035d)
        .range(0.01d, 0.075d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.SemiControl)
        .build()
    );

    public final Setting<Double> maxSpeedSemiControl = sgSemiControl.add(new DoubleSetting.Builder()
        .name("Max Speed")
        .description("Max BPS.")
        .defaultValue(48.0d)
        .min(10.0d)
        .sliderMax(100.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.SemiControl)
        .build()
    );

    public final Setting<Double> maxSpeedPacket = sgPacket.add(new DoubleSetting.Builder()
        .name("Max Speed")
        .description("Max BPS.")
        .defaultValue(70.0d)
        .min(10.0d)
        .sliderMax(100.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Packet)
        .build()
    );

    public final Setting<Double> accelerationPacket = sgPacket.add(new DoubleSetting.Builder()
        .name("Acceleration")
        .description("The speed of acceleration.")
        .defaultValue(0.06d)
        .range(0.01d, 0.09d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Packet)
        .build()
    );

    public final Setting<Double> maxSpeedBoost = sgBoost.add(new DoubleSetting.Builder()
        .name("Max Speed")
        .description("Max BPS.")
        .defaultValue(45.0d)
        .min(10.0d)
        .sliderMax(100.0d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Boost)
        .build()
    );

    public final Setting<Double> accelerationBoost = sgBoost.add(new DoubleSetting.Builder()
        .name("Acceleration")
        .description("The speed of acceleration.")
        .defaultValue(0.045d)
        .range(0.01d, 0.08d)
        .visible(() -> flightMode.get() == ElytraFlyPlusModes.Boost)
        .build()
    );

    public ElytraFlyPlus() {
        super(Addon.CATEGORY, "elytra-fly-plus", "Better elytrafly");
    }

    @Override
    public void onActivate() {
        super.onActivate();
        currentMode.onActivate();
    }

    @Override
    public void onDeactivate() {
        super.onDeactivate();
        currentMode.onDeactivate();
    }

    double frozenMotionX, frozenMotionY, frozenMotionZ;
    boolean isFrozen = false;

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        currentMode.onPreTick(event);
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        currentMode.onPostTick(event);
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        currentMode.onSendPacket(event);
    }

    @Override
    public String getInfoString() {
        return currentMode.getHudString();
    }

}
