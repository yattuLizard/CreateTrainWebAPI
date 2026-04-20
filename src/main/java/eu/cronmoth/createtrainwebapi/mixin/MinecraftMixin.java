package eu.cronmoth.createtrainwebapi.mixin;

import eu.cronmoth.createtrainwebapi.CreateTrainWebAPIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(RunArgs args, CallbackInfo ci) {
        CreateTrainWebAPIMod.LOGGER.info("Hello from {}", CreateTrainWebAPIMod.NAME);
    }
}