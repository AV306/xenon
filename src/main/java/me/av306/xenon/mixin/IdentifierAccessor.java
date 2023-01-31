package me.av306.xenon.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( Identifier.class )
public interface IdentifierAccessor
{
	@Accessor( "namespace" )
	void setNamespace( String namespace );
}
