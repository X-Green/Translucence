package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidRenderer.class)
public abstract class MixinFluidRenderer {
    @Redirect(method = "isSideCovered", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOpaque()Z"))
    private static boolean redirectedIsOpaque$shouldDrawSide(BlockState blockState) {
        if (((IBlockState)blockState).isTransparent()) {
            return false;
        } else {
            return blockState.isOpaque();
        }
    }
}
