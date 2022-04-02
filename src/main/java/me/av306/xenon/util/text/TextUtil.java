package me.av306.xenon.util.text;

import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.Xenon;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtil
{
	// TODO: finish implementing
	public static void drawPositionedText( MatrixStack matrices, Text text, ScreenPosition position, int xOffset, int yOffset, boolean shadow, int color )
	{
		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;
		int scaledWidth = Xenon.INSTANCE.client.getWindow().getScaledWidth();
		int scaledHeight = Xenon.INSTANCE.client.getWindow().getScaledHeight();
		int x = 5 + xOffset, y = 5 + yOffset;
		switch ( position )
		{
			case TOP_LEFT:
				break;
				
			case TOP_CENTER:
				// calculate x
				x = (scaledWidth - textRenderer.getWidth( text )) / 2;
				break;
				
			case TOP_RIGHT:
				// calculate x
			  	x = scaledWidth - textRenderer.getWidth( text );
				break;
				
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				break;
		}

		if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
		else textRenderer.draw( matrices, text, x, y, color );
	}

	public static void drawPositionedMultiLineText( MatrixStack matrices, Text[] texts, ScreenPosition position, int xOffset, int yOffset, int color )
	{
		// REMINDER:
		// Y is UP-DOWN offset
		// X is LEFT-RIGHT offset
		// I'm dumb

		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;
		int scaledWidth = Xenon.INSTANCE.client.getWindow().getScaledWidth();
		int scaledHeight = Xenon.INSTANCE.client.getWindow().getScaledHeight();

		int x = 5 + xOffset, y = 5 + yOffset;
		switch ( position )
		{
			case TOP_LEFT:
				for ( Text text : texts )
				{
          			textRenderer.drawWithShadow( matrices, text, x, y, color );

          			y += 12;
			  	}
				break;
				
			case TOP_CENTER:
		    	for ( Text text : texts )
        		{
					x = (scaledWidth - textRenderer.getWidth( text )) / 2;
					
          			textRenderer.drawWithShadow( matrices, text, x, y, color );

          			y += 12;
        		}
		    	break;
				
			case TOP_RIGHT:
				for ( Text text : texts )
        		{
					x = scaledWidth - textRenderer.getWidth( text ) - x;
					
          			textRenderer.drawWithShadow( matrices, text, x, y, color );

          			y += 12;
			  	}
				break;
				
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				break;
		}
	}
}