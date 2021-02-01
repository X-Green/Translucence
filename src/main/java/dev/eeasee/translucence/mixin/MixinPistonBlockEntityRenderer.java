package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.PistonBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntityRenderer.class)
public abstract class MixinPistonBlockEntityRenderer {

    @Inject(method = "method_3575", at = @At("HEAD"), cancellable = true)
    private void beforePistonEntityRenderBlock(BlockPos blockPos, BlockState blockState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, World world, boolean bl, int i, CallbackInfo ci) {
        if (((IBlockState) blockState).isTransparent()) {
            ci.cancel();
        }
    }
}
