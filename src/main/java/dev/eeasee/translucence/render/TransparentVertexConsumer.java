package dev.eeasee.translucence.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TransparentVertexConsumer implements VertexConsumer {

    private final VertexConsumer host;
    private final float alpha;

    public TransparentVertexConsumer(VertexConsumer host, float alpha) {
        this.host = host;
        this.alpha = alpha;
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        int[] is = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Vector3f vector3f = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
        Matrix4f matrix4f = matrixEntry.getModel();
        vector3f.transform(matrixEntry.getNormal());
        int j = is.length / 8;
        MemoryStack memoryStack = MemoryStack.stackPush();
        Throwable var17 = null;

        try {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSize());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for(int k = 0; k < j; ++k) {
                intBuffer.clear();
                intBuffer.put(is, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float r;
                float s;
                float t;
                float v;
                float w;
                if (useQuadColorData) {
                    float l = (float)(byteBuffer.get(12) & 255) / 255.0F;
                    v = (float)(byteBuffer.get(13) & 255) / 255.0F;
                    w = (float)(byteBuffer.get(14) & 255) / 255.0F;
                    r = l * brightnesses[k] * red;
                    s = v * brightnesses[k] * green;
                    t = w * brightnesses[k] * blue;
                } else {
                    r = brightnesses[k] * red;
                    s = brightnesses[k] * green;
                    t = brightnesses[k] * blue;
                }

                int u = lights[k];
                v = byteBuffer.getFloat(16);
                w = byteBuffer.getFloat(20);
                Vector4f vector4f = new Vector4f(f, g, h, 1.0F);
                vector4f.transform(matrix4f);
                this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), r, s, t, this.alpha, v, w, overlay, u, vector3f.getX(), vector3f.getY(), vector3f.getZ());
            }
        } catch (Throwable var38) {
            var17 = var38;
            throw var38;
        } finally {
            if (memoryStack != null) {
                if (var17 != null) {
                    try {
                        memoryStack.close();
                    } catch (Throwable var37) {
                        var17.addSuppressed(var37);
                    }
                } else {
                    memoryStack.close();
                }
            }

        }

    }


    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return host.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return host.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return host.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return host.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return host.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return host.normal(x, y, z);
    }

    @Override
    public void next() {
        host.next();
    }
}
