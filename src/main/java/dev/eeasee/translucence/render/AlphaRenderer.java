package dev.eeasee.translucence.render;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.render.shader.ShaderProgram;
import fi.dy.masa.malilib.util.Color4f;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class AlphaRenderer {
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

        // drawBlockBoundingBoxOutlinesBatchedLines(pos, color, expand, buffer, mc);
        {
            Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
            final double dx = cameraPos.x;
            final double dy = cameraPos.y;
            final double dz = cameraPos.z;

            double minX = pos.getX() - dx - expand;
            double minY = pos.getY() - dy - expand;
            double minZ = pos.getZ() - dz - expand;
            double maxX = pos.getX() - dx + expand + 1;
            double maxY = pos.getY() - dy + expand + 1;
            double maxZ = pos.getZ() - dz + expand + 1;

            /*
            voxelShape.forEachEdge((k, l, m, n, o, p) -> {
                vertexConsumer.vertex(matrix4f, (float) (k + d), (float) (l + e), (float) (m + f)).color(g, h, i, j).next();
                vertexConsumer.vertex(matrix4f, (float) (n + d), (float) (o + e), (float) (p + f)).color(g, h, i, j).next();
            });
             */

            // fi.dy.masa.malilib.render.RenderUtils.drawBoxAllEdgesBatchedLines(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
            {
                // West side
                buffer.vertex(minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();

                // East side
                buffer.vertex(maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();

                // North side (don't repeat the vertical lines that are done by the east/west sides)
                buffer.vertex(maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();

                // South side (don't repeat the vertical lines that are done by the east/west sides)
                buffer.vertex(minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();

                buffer.vertex(maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
                buffer.vertex(minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
            }

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


    public static void drawBlockBoundingBoxOutlinesBatchedLines(BlockPos pos, Color4f color,
                                                                double expand, BufferBuilder buffer, MinecraftClient mc) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        final double dx = cameraPos.x;
        final double dy = cameraPos.y;
        final double dz = cameraPos.z;

        double minX = pos.getX() - dx - expand;
        double minY = pos.getY() - dy - expand;
        double minZ = pos.getZ() - dz - expand;
        double maxX = pos.getX() - dx + expand + 1;
        double maxY = pos.getY() - dy + expand + 1;
        double maxZ = pos.getZ() - dz + expand + 1;

        fi.dy.masa.malilib.render.RenderUtils.drawBoxAllEdgesBatchedLines(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
    }

    public static void renderAreaSidesBatched(BlockPos pos1, BlockPos pos2, Color4f color,
                                              double expand, BufferBuilder buffer, MinecraftClient mc) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        final double dx = cameraPos.x;
        final double dy = cameraPos.y;
        final double dz = cameraPos.z;
        double minX = Math.min(pos1.getX(), pos2.getX()) - dx - expand;
        double minY = Math.min(pos1.getY(), pos2.getY()) - dy - expand;
        double minZ = Math.min(pos1.getZ(), pos2.getZ()) - dz - expand;
        double maxX = Math.max(pos1.getX(), pos2.getX()) + 1 - dx + expand;
        double maxY = Math.max(pos1.getY(), pos2.getY()) + 1 - dy + expand;
        double maxZ = Math.max(pos1.getZ(), pos2.getZ()) + 1 - dz + expand;

        fi.dy.masa.malilib.render.RenderUtils.drawBoxAllSidesBatchedQuads(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
    }

    private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        voxelShape.forEachEdge((k, l, m, n, o, p) -> {
            vertexConsumer.vertex(matrix4f, (float) (k + d), (float) (l + e), (float) (m + f)).color(g, h, i, j).next();
            vertexConsumer.vertex(matrix4f, (float) (n + d), (float) (o + e), (float) (p + f)).color(g, h, i, j).next();
        });
    }


    public static void onRenderWorldLast(BufferBuilderStorage bufferBuilderStorage, MatrixStack matrices, MinecraftClient client, float tickDelta) {
        VertexConsumerProvider vertexConsumerProvider = bufferBuilderStorage.getEntityVertexConsumers();

        /*
        Vec3d vec3d = client.cameraEntity.getCameraPosVec(tickDelta);
        double d = vec3d.getX();
        double e = vec3d.getY();
        double f = vec3d.getZ();

        drawBlockOutline(client.world, matrices, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), client.player, d, e, f, new BlockPos(-123,66,-123), Blocks.ANVIL.getDefaultState());


         */


        RenderSystem.pushMatrix();

        fi.dy.masa.malilib.render.RenderUtils.color(1f, 1f, 1f, 1f);
        fi.dy.masa.malilib.render.RenderUtils.setupBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);
        RenderSystem.disableTexture();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.01F);

        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.2f, -0.2f);

        float expand = 0.001f;
        float lineWidthBlockBox = 2f;
        Color4f color4f = new Color4f(50, 50, 200, 100);
        if (client.player == null) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        BlockPos origin = new BlockPos(-123, 64, -123);
        renderBlockOutline(origin, expand, lineWidthBlockBox, color4f, client);


        RenderSystem.polygonOffset(0f, 0f);
        RenderSystem.disablePolygonOffset();

        RenderSystem.popMatrix();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
    }
}
