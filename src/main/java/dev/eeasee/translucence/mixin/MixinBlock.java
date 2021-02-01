package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlock;
import dev.eeasee.translucence.fakes.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(Block.class)
public abstract class MixinBlock implements IBlock {
    @Final
    @Shadow
    @Mutable
    protected StateManager<Block, BlockState> stateManager;

    @Shadow
    protected abstract void appendProperties(StateManager.Builder<Block, BlockState> builder);

    @Shadow
    private BlockState defaultState;

    @Override
    public void appendNewBlockStates(Consumer<StateManager.Builder<Block, BlockState>> appendNewProperties, Function<BlockState, BlockState> appendDefaultState) {
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder<>((Block) (Object) this);
        this.appendProperties(builder);
        appendNewProperties.accept(builder);
        this.stateManager = builder.build(BlockState::new);
        this.defaultState = appendDefaultState.apply(this.stateManager.getDefaultState());
    }

    /*
    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    private void isTranslucent(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (((IBlockState) state).isTransparent()) {
            cir.setReturnValue(true);
        }
    }

     */

    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void ambientOcclusionLightLevel(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (((IBlockState) state).isTransparent()) {
            cir.setReturnValue(1.0F);
        }
    }

    @Redirect(method = "shouldDrawSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOpaque()Z"))
    private static boolean redirectedIsOpaque$shouldDrawSide(BlockState blockState) {
        if (((IBlockState)blockState).isTransparent()) {
            return false;
        } else {
            return blockState.isOpaque();
        }
    }
}
