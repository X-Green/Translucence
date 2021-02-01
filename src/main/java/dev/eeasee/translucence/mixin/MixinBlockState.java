package dev.eeasee.translucence.mixin;

import com.google.common.collect.ImmutableMap;
import dev.eeasee.translucence.fakes.IBlockState;
import dev.eeasee.translucence.property.PropertyRenderingOpacity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.AbstractState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockState.class)
public abstract class MixinBlockState extends AbstractState<Block, BlockState> implements State<BlockState>, IBlockState {

    @Shadow
    public abstract Block getBlock();

    protected MixinBlockState(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries) {
        super(owner, entries);
    }

    private boolean transparent = false;

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void invisibleRenderType(CallbackInfoReturnable<BlockRenderType> cir) {
        if (this.transparent) {
            cir.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initTranslucence(Block block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, CallbackInfo ci) {
        if (propertyMap.containsKey(PropertyRenderingOpacity.RENDERING_OPACITY)) {
            if (propertyMap.get(PropertyRenderingOpacity.RENDERING_OPACITY) != PropertyRenderingOpacity.NORMAL) {
                this.transparent = true;
            }
        }
    }


    @Override
    public boolean isTransparent() {
        return this.transparent;
    }

}
