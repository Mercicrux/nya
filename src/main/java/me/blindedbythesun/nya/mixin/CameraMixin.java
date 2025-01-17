package me.blindedbythesun.nya.mixin;

import me.blindedbythesun.nya.modules.efly.ElytraFlyPlus;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlusModes;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusSemiControl;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusSimple;
import me.blindedbythesun.nya.modules.efly.impl.ElytraFlyPlusTest;
import meteordevelopment.meteorclient.mixininterface.ICamera;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(Camera.class)
public abstract class CameraMixin implements ICamera {

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void onUpdateSetRotationArgs(Args args) {
        ElytraFlyPlus elytraFlyPlus = Modules.get().get(ElytraFlyPlus.class);

        if (elytraFlyPlus.isActive() && elytraFlyPlus.flightMode.get() == ElytraFlyPlusModes.Simple && mc.player.isFallFlying()) {
            ElytraFlyPlusSimple eflyMode = (ElytraFlyPlusSimple) elytraFlyPlus.currentMode;
            args.set(1, eflyMode.cameraPitch);
        }else if (elytraFlyPlus.isActive() && elytraFlyPlus.flightMode.get() == ElytraFlyPlusModes.SemiControl && mc.player.isFallFlying()) {
            ElytraFlyPlusSemiControl eflyMode = (ElytraFlyPlusSemiControl) elytraFlyPlus.currentMode;
            args.set(0, eflyMode.cameraYaw);
            args.set(1, eflyMode.cameraPitch);
        }
    }

}
