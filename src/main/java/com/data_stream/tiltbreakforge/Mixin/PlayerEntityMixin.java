package com.data_stream.tiltbreakforge.Mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Shadow
    protected float hurtDir;

    /**
     * @author StyStatic
     * @reason Modify tilt function
     */

    @Inject(at = @At("RETURN"), method = "animateHurt")
    private void modifyTiltYaw(float yaw, CallbackInfo ci) {
        this.hurtDir = 0F; // Thanks unilock01x for the correct Yaw value
    }
}
