package dev.eeasee.translucence.property;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum PropertyRenderingOpacity implements StringIdentifiable {
    NORMAL("normal"),
    OUTLINE("outline"),
    TRANSLUCENT("translucent"),
    INVISIBLE("invisible");

    public static EnumProperty<PropertyRenderingOpacity> RENDERING_OPACITY = EnumProperty.of("rendering_opacity", PropertyRenderingOpacity.class);

    private final String name;

    PropertyRenderingOpacity(String s) {
        this.name = s;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
