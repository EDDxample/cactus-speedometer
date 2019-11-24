package eddxample.mixin;

import eddxample.CactusSpeedometer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerTickMixin {
    @Shadow private int ticks;

    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(BooleanSupplier f, CallbackInfo ci) {
        CactusSpeedometer.updateTimestamp(ticks);
    }
}
