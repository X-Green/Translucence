package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import fi.dy.masa.malilib.util.Color4f;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

import static dev.eeasee.translucence.render.AlphaRenderer.renderBlockOutline;

@Mixin(BlockRenderManager.class)
public abstract class MixinBlockRenderManager {

    /*
    @Redirect(method = "renderBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/Random;JI)Z"))
    private boolean redirectedRender$renderBlock(BlockModelRenderer blockModelRenderer, BlockRenderView view, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay) {
        if (((IBlockState) state).isTransparent()) {

            // VertexConsumer transparentVertexConsumer = new TransparentVertexConsumer(vertexConsumer, 0.5F);
            // boolean bl = blockModelRenderer.render(view, this.getModel(state), state, pos, matrix, transparentVertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);

            MinecraftClient client = MinecraftClient.getInstance();
            float expand = 0.001f;
            float lineWidthBlockBox = 2f;
            Color4f color4f = new Color4f(50, 50, 200, 100);
            if (client.player == null) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } else {
                BlockPos origin = new BlockPos(-123, 64, -123);
                renderBlockOutline(origin, expand, lineWidthBlockBox, color4f, client);
            }

            return true;
        } else {
            return blockModelRenderer.render(view, this.getModel(state), state, pos, matrix, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        }
    }

     */
}
