package dev.eeasee.translucence.mixin;

import dev.eeasee.translucence.fakes.IBlockState;
import dev.eeasee.translucence.fakes.IWorldRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk$RebuildTask")
public abstract class MixinChunkBuilder$BuiltChunk$RebuildTask {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private BlockState redirectedGetBlockState$Render(ChunkRendererRegion chunkRendererRegion, BlockPos pos) {
        BlockState blockState = chunkRendererRegion.getBlockState(pos);
        if (!blockState.isAir()) {
            System.out.println(blockState);
        }
        if (((IBlockState) blockState).isOutlined()) {
            ((IWorldRenderer) MinecraftClient.getInstance().worldRenderer).getOutlineRenderer().tryAddToSection(
                    pos, blockState
            );
        }
        return blockState;
    }
}
