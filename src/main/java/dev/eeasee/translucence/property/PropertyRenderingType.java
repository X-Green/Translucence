package dev.eeasee.translucence.property;

import com.sun.istack.internal.Nullable;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum PropertyRenderingType implements StringIdentifiable {
    NORMAL      ("normal"       , false, false, null),
    OUTLINE     ("outline"      , true , true , BlockRenderType.INVISIBLE),
    TRANSLUCENT ("translucent"  , true , false, BlockRenderType.MODEL),
    INVISIBLE   ("invisible"    , true , false, BlockRenderType.INVISIBLE);

    public static EnumProperty<PropertyRenderingType> RENDERING_TYPE = EnumProperty.of("rendering_type", PropertyRenderingType.class);

    public final String name;
    public final boolean transparent;
    public final boolean outlined;
    @Nullable
    public final BlockRenderType renderType;
    public final boolean notNormal;


    PropertyRenderingType(String name, boolean transparent, boolean outlined, BlockRenderType renderType) {
        this.name = name;
        this.transparent = transparent;
        this.outlined = outlined;
        this.renderType = renderType;
        this.notNormal = !name.equals("normal");
    }


    @Override
    public String asString() {
        return this.name;
    }
}
