package dev.eeasee.translucence.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.translucence.config.Configs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class RenderUtil {
    public static class LineRenderUtil {
        public static BufferBuilder setupRenderBlockOutline() {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
            return buffer;
        }

        public static void setLineWidth(float lineWidth) {
            RenderSystem.lineWidth(lineWidth);
        }

        public static void endRenderBlockOutline() {
            Tessellator.getInstance().draw();
        }

        public static void vertexBlockOutlineBuffer(BlockPos pos, BufferBuilder bufferBuilder, Color4f color, MinecraftClient mc) {
            Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
            double dx = cameraPos.x;
            double dy = cameraPos.y;
            double dz = cameraPos.z;

            Blocks.ANVIL.getDefaultState().getOutlineShape(mc.world, pos).forEachEdge((k, l, m, n, o, p) -> {
                bufferBuilder.vertex(
                        (float) k + pos.getX() - dx,
                        (float) l + pos.getY() - dy,
                        (float) m + pos.getZ() - dz
                ).color(color.r, color.g, color.b, color.a).next();
                bufferBuilder.vertex(
                        (float) n + pos.getX() - dx,
                        (float) o + pos.getY() - dy,
                        (float) p + pos.getZ() - dz
                ).color(color.r, color.g, color.b, color.a).next();
            });
        }

        public static void renderBlocksOutlined(Map<BlockState, Iterable<BlockPos>> blockStatePosMap) {
            BufferBuilder bufferBuilder = setupRenderBlockOutline();
            MinecraftClient client = MinecraftClient.getInstance();
            for (Map.Entry<BlockState, Iterable<BlockPos>> entry: blockStatePosMap.entrySet()) {
                BlockState state = entry.getKey();
                Iterable<BlockPos> blockPosIterable = entry.getValue();
                setLineWidth(Configs.INSTANCE.getLineWidth(state));
                Color4f color = Configs.INSTANCE.getColor(state);
                for (BlockPos pos:blockPosIterable) {
                    vertexBlockOutlineBuffer(pos, bufferBuilder, color, client);
                }
            }
            endRenderBlockOutline();
        }
    }
}
