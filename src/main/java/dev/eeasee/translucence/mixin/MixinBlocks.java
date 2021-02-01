package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.config.Configs;
import dev.eeasee.translucence.fakes.IBlock;
import dev.eeasee.translucence.property.PropertyRenderingOpacity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Blocks.class)
public abstract class MixinBlocks {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void applyCustomProperties(CallbackInfo ci) {
        Set<Identifier> translucent = Configs.INSTANCE.AFFECTED_BLOCK_ID_SET;
        translucent.forEach(identifier -> {
            Block target = (Registry.BLOCK.get(identifier));
            ((IBlock) target).appendNewBlockStates(
                    builder -> builder.add(PropertyRenderingOpacity.RENDERING_OPACITY),
                    blockState -> blockState.with(PropertyRenderingOpacity.RENDERING_OPACITY, PropertyRenderingOpacity.NORMAL)
            );
        });
    }
}
