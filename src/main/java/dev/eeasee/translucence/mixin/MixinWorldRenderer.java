package dev.eeasee.translucence.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private ClientWorld world;

    @Shadow
    protected abstract void renderLayer(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f);

    @Inject(method = "render", at = @At(value = "INVOKE", ordinal = 3, shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDD)V"))
    private void renderLayerTranslucent(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

    }


    @Inject(method = "reload()V", at = @At("RETURN"))
    private void onLoadRenderers(CallbackInfo ci) {
        // Also (re-)load our renderer when the vanilla renderer gets reloaded
        if (this.world != null && this.world == net.minecraft.client.MinecraftClient.getInstance().world) {
            // AlphaRenderer.init();
        }
    }


    @Inject(method = "render",
            at = @At(value = "INVOKE_STRING", args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void onRenderWorldLast(
            net.minecraft.client.util.math.MatrixStack matrices,
            float tickDelta, long limitTime, boolean renderBlockOutline,
            net.minecraft.client.render.Camera camera,
            net.minecraft.client.render.GameRenderer gameRenderer,
            net.minecraft.client.render.LightmapTextureManager lightmapTextureManager,
            net.minecraft.client.util.math.Matrix4f matrix4f,
            CallbackInfo ci) {
        // AlphaRenderer.onRenderWorldLast(matrices, this.client, tickDelta);
    }
}
