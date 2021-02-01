package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeavesBlock.class)
public abstract class MixinLeavesBlock {
    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOpaque()Z"))
    private boolean redirectedIsOpaque$randomDisplayTick(BlockState blockState) {
        if (((IBlockState)blockState).isTransparent()) {
            return false;
        } else {
            return blockState.isOpaque();
        }
    }
}
