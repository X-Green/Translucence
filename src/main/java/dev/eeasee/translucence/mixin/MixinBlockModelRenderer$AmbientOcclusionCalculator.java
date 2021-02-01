package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.render.block.BlockModelRenderer$AmbientOcclusionCalculator")
public abstract class MixinBlockModelRenderer$AmbientOcclusionCalculator {
    @Redirect(method = "apply", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getOpacity(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I"
    ))
    private int getOpacity(BlockState blockState, BlockView view, BlockPos pos) {
        if (((IBlockState)blockState).isTransparent()) {
            return 0;
        } else {
            return blockState.getOpacity(view, pos);
        }
    }

}
