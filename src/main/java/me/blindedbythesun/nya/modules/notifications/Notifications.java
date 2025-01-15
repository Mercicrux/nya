package me.blindedbythesun.nya.modules.notifications;

import me.blindedbythesun.nya.Addon;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;

public class Notifications extends Module {

    public Notifications() {
        super(Addon.CATEGORY, "notifications", "fancy notifications");
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    public final Setting<Double> notificationScale = sgGeneral.add(new DoubleSetting.Builder()
        .name("Scale")
        .description("Size of notifications")
        .defaultValue(2.0d)
        .min(1.0d)
        .sliderMax(4.0d)
        .build()
    );

    private ArrayList<Notification> notifications = new ArrayList<>();

    public void addNotification(String name, String description, long duration, NotificationType type) {
        notifications.add(new Notification(name, description, duration, type));
    }

    public void addNotification(String description, long duration, NotificationType type) {
        notifications.add(new Notification(description, duration, type));
    }

    @Override
    public void onActivate() {
        notifications.clear();
    }

    @EventHandler
    private void onRender(Render2DEvent event) {
        int posX = mc.getWindow().getWidth()/2, posY = mc.getWindow().getHeight()/2 + 16;
        double scaleFactor = mc.getWindow().getScaleFactor();
        for(int i = 0; i < notifications.size(); i++) {
            Notification n = notifications.get(i);
            if(System.currentTimeMillis() > n.getCreationTime() + n.getDurationMS()) {
                notifications.remove(i);
                i--;
            }
        }
        for(Notification n : notifications) {
            int width = Math.max(100, Math.max(mc.textRenderer.getWidth(n.getName()) + 4, mc.textRenderer.getWidth(n.getDescription()) + 4)), height = 21;
            if(n.getName().equalsIgnoreCase("")) {
                height = 13;
            }
            int scaledWidth = (int) (width * notificationScale.get()), scaledHeight = (int) (height * notificationScale.get());
            event.drawContext.getMatrices().push();
            event.drawContext.getMatrices().translate(posX, posY + (height / 2), 0);

            float animTime = 175;
            if(System.currentTimeMillis() - n.getCreationTime() < animTime) {
                float time = System.currentTimeMillis() - n.getCreationTime();
                event.drawContext.getMatrices().scale(time/animTime, time/animTime, 0);
            }else if((n.getCreationTime() + n.getDurationMS()) - System.currentTimeMillis() < animTime) {
                float time = (n.getCreationTime() + n.getDurationMS()) - System.currentTimeMillis();
                event.drawContext.getMatrices().scale(time/animTime, time/animTime, 0);
            }

            event.drawContext.getMatrices().translate(-(scaledWidth / 2), -(height / 2), 0);
            event.drawContext.getMatrices().scale(notificationScale.get().floatValue(), notificationScale.get().floatValue(), 0);

            double progressMult = Math.min(1, (double)(System.currentTimeMillis() - n.getCreationTime()) / (double)n.getDurationMS());
            event.drawContext.fill(0, 0, width, height, 0x66000000);
            event.drawContext.fill(0, height - 1, (int) ((1-progressMult) * width), height, 0xFF000000 + n.getType().color);
            if(n.getName().equalsIgnoreCase("")) {
                event.drawContext.drawTextWithShadow(mc.textRenderer, n.getDescription(), 2, 2, -1);
            }else {
                event.drawContext.drawTextWithShadow(mc.textRenderer, n.getName(), (width/2) - (mc.textRenderer.getWidth(n.getName())/2), 2, n.getType().color);
                event.drawContext.drawTextWithShadow(mc.textRenderer, n.getDescription(), (width/2) - (mc.textRenderer.getWidth(n.getDescription())/2), 11, -1);
            }

            event.drawContext.getMatrices().pop();
            posY += scaledHeight + 4;
        }
    }

}
