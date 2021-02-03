package dev.eeasee.translucence.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.translucence.fakes.IWorldRenderer;
import dev.eeasee.translucence.util.Color4f;
import fi.dy.masa.malilib.render.shader.ShaderProgram;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class AddedOnWorldLastRenderer {
    private static final ShaderProgram SHADER_ALPHA = new ShaderProgram("translucence", null, "shaders/alpha.frag");

    static {

    }

    public static void init() {
        int program = SHADER_ALPHA.getProgram();
        GL20.glUseProgram(program);
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "texture"), 0);
        GL20.glUseProgram(0);
    }

    public static void enableAlphaShader() {
        float alpha = 0.7F;
        GL20.glUseProgram(SHADER_ALPHA.getProgram());
        GL20.glUniform1f(GL20.glGetUniformLocation(SHADER_ALPHA.getProgram(), "alpha_multiplier"), alpha);
    }

    public static void disableAlphaShader() {
        GL20.glUseProgram(0);
    }

    public static void renderBlockOutline(BlockPos pos, float expand, float lineWidth, Color4f color, MinecraftClient mc) {
        RenderSystem.lineWidth(lineWidth);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);

        {
            Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
            double dx = cameraPos.x;
            double dy = cameraPos.y;
            double dz = cameraPos.z;

            Blocks.ANVIL.getDefaultState().getOutlineShape(mc.world, pos).forEachEdge((k, l, m, n, o, p) -> {
                buffer.vertex(
                        (float) k + pos.getX() - dx - expand,
                        (float) l + pos.getY() - dy - expand,
                        (float) m + pos.getZ() - dz - expand
                ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(
                        (float) n + pos.getX() - dx + expand,
                        (float) o + pos.getY() - dy + expand,
                        (float) p + pos.getZ() - dz + expand
                ).color(color.r, color.g, color.b, color.a).next();
            });
        }

        tessellator.draw();
    }


    public static void drawBlockOutline(World world, MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, BlockPos blockPos, BlockState blockState) {

        Vec3d camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        /*
        drawShapeOutline(matrixStack,
                vertexConsumer,
                blockState.getOutlineShape(world, blockPos),
                (double) blockPos.getX() - camPos.getX(),
                (double) blockPos.getY() - camPos.getY(),
                (double) blockPos.getZ() - camPos.getZ(),
                0.0F, 0.0F, 0.0F, 0.4F);

         */
        Matrix4f matrix4f = matrixStack.peek().getModel();
        blockState.getOutlineShape(world, blockPos).forEachEdge((k, l, m, n, o, p) -> {
            vertexConsumer.vertex(matrix4f, (float) (k + ((double) blockPos.getX() - camPos.getX())), (float) (l + ((double) blockPos.getY() - camPos.getY())), (float) (m + ((double) blockPos.getZ() - camPos.getZ()))).color(0, 0, 0, 0.4f).next();
            vertexConsumer.vertex(matrix4f, (float) (n + ((double) blockPos.getX() - camPos.getX())), (float) (o + ((double) blockPos.getY() - camPos.getY())), (float) (p + ((double) blockPos.getZ() - camPos.getZ()))).color(0, 0, 0, 0.4f).next();
        });
    }


    private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        voxelShape.forEachEdge((k, l, m, n, o, p) -> {
            vertexConsumer.vertex(matrix4f, (float) (k + d), (float) (l + e), (float) (m + f)).color(g, h, i, j).next();
            vertexConsumer.vertex(matrix4f, (float) (n + d), (float) (o + e), (float) (p + f)).color(g, h, i, j).next();
        });
    }


    public static void onRenderWorldLast(WorldRenderer worldRenderer, MatrixStack matrices, float tickDelta) {
        RenderSystem.pushMatrix();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);

        RenderSystem.enableDepthTest();
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);
        RenderSystem.disableTexture();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.01F);

        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.2f, -0.2f);

        BlockPos origin = new BlockPos(-123, 64, -123);

        OutlineRenderer outlineRenderer = ((IWorldRenderer) worldRenderer).getOutlineRenderer();
        outlineRenderer.tryAddToSection(origin, Blocks.ANVIL.getDefaultState());
        outlineRenderer.renderAllOutlines();

        RenderSystem.polygonOffset(0f, 0f);
        RenderSystem.disablePolygonOffset();

        RenderSystem.popMatrix();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
    }
}
