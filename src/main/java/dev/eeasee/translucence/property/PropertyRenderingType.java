package dev.eeasee.translucence.property;

import com.sun.istack.internal.Nullable;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum PropertyRenderingType implements StringIdentifiable {
    NORMAL      ("normal"       , false, false),
    OUTLINE     ("outline"      , true , true ),
    TRANSLUCENT ("translucent"  , true , false),
    INVISIBLE   ("invisible"    , true , false);

    public static EnumProperty<PropertyRenderingType> RENDERING_TYPE = EnumProperty.of("rendering_type", PropertyRenderingType.class);

    public final String name;
    public final boolean transparent;
    public final boolean outlined;
    public final boolean notNormal;


    PropertyRenderingType(String name, boolean transparent, boolean outlined) {
        this.name = name;
        this.transparent = transparent;
        this.outlined = outlined;
        this.notNormal = !name.equals("normal");
    }


    @Override
    public String asString() {
        return this.name;
    }
}
