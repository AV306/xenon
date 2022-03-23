package me.av306.xenon.util.text;

import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.Xenon;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtil
{
	// TODO: Maybe put text drawing logic here someday? im too lazy rn :P
	public static void drawPositionedText( MatrixStack matrices, Text text, ScreenPosition position, int color )
	{
		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;
		switch ( position )
		{
			case TOP_LEFT:
			case TOP_CENTER:
			case TOP_RIGHT:
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				break;
		}
	}
}