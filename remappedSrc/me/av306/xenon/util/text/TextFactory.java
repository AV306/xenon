package me.av306.xenon.util.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TextFactory
{
    public static MutableText createEmpty()
    {
        return Text.empty();
    }

    public static MutableText createTranslatable( String key )
    {
        return Text.translatable( key );
    }

    public static MutableText createTranslatable( String key, Object... args )
    {
        return Text.translatable( key, args );
    }

    public static MutableText createLiteral( String content )
    {
        return Text.literal( content );
    }
}
