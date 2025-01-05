package me.sun.unnamed.mixin;

import me.sun.unnamed.Addon;
import me.sun.unnamed.modules.notifications.NotificationType;
import meteordevelopment.meteorclient.systems.modules.Module;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Module.class, remap = false)
public abstract class ModuleMixin {

    @Final
    @Shadow
    public String title;
    @Shadow
    public abstract boolean isActive();

    @Inject(method = "sendToggledMsg", at = @At("RETURN"))
    public void sendToggledMsg(CallbackInfo ci) {
        if (Addon.notifications.isActive()) {
            Addon.notifications.addNotification(String.format("%s is now %s§r.", title, isActive() ? "§aenabled" : "§cdisabled"), 2000, NotificationType.INFO);
        }
    }

}
