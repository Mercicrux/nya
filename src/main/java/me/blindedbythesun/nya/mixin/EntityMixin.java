package me.blindedbythesun.nya.mixin;

import me.blindedbythesun.nya.modules.efly.ElytraFlyPlus;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusModes;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusSemiControl;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusSimple;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusTest;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(Entity.class)
public abstract class EntityMixin {


    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void updateChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this != mc.player) return;

        ElytraFlyPlus elytraFlyPlus = Modules.get().get(ElytraFlyPlus.class);

        if (elytraFlyPlus.isActive() && elytraFlyPlus.flightMode.get() == ElytraFlyPlusModes.Simple && mc.player.isFallFlying()) {
            ElytraFlyPlusSimple eflyMode = (ElytraFlyPlusSimple) elytraFlyPlus.currentMode;

            float g = (float)cursorDeltaX * 0.15F;
            mc.player.setYaw(mc.player.getYaw() + g);
            mc.player.prevYaw += g;

            eflyMode.cameraPitch += (float) (cursorDeltaY * 0.15F);

            if (Math.abs(eflyMode.cameraPitch) > 90.0F) eflyMode.cameraPitch = eflyMode.cameraPitch > 0.0F ? 90.0F : -90.0F;

            ci.cancel();
        }else if (elytraFlyPlus.isActive() && elytraFlyPlus.flightMode.get() == ElytraFlyPlusModes.SemiControl && mc.player.isFallFlying()) {
            ElytraFlyPlusSemiControl eflyMode = (ElytraFlyPlusSemiControl) elytraFlyPlus.currentMode;

            eflyMode.cameraYaw += (float) (cursorDeltaX * 0.15F);
            eflyMode.cameraPitch += (float) (cursorDeltaY * 0.15F);

            if (Math.abs(eflyMode.cameraPitch) > 90.0F) eflyMode.cameraPitch = eflyMode.cameraPitch > 0.0F ? 90.0F : -90.0F;

            ci.cancel();
            /*freeLook.cameraYaw += (float) (cursorDeltaX / freeLook.sensitivity.get().floatValue());
            freeLook.cameraPitch += (float) (cursorDeltaY / freeLook.sensitivity.get().floatValue());

            if (Math.abs(freeLook.cameraPitch) > 90.0F) freeLook.cameraPitch = freeLook.cameraPitch > 0.0F ? 90.0F : -90.0F;
            ci.cancel();*/
        }
    }
}
