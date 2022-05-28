package me.av306.xenon.util.text;

import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.Xenon;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Helper class for text-related operations.
 *
 * Contains methods for drawing text at a predefined position,
 * as well as appending lines to text.
 *
 * TODO: implement BOTTOM_* text drawing
 */
public class TextUtil
{
	public static void drawPositionedText(
			MatrixStack matrices,
			Text text,
			ScreenPosition position,
			int xOffset,
			int yOffset,
			boolean shadow,
			int color
	)
	{
		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;

		int scaledWidth = Xenon.INSTANCE.client.getWindow().getScaledWidth();
		int scaledHeight = Xenon.INSTANCE.client.getWindow().getScaledHeight();

		int x = 5 + xOffset, y = 5 + yOffset;

		// the cases are designed to fall through to each other
		// to minimise repeated code.
		switch ( position )
		{
			case BOTTOM_LEFT:
				// calculate y
				y = scaledHeight - 10;
			case TOP_LEFT:
				// ok, skip
				break;

			case BOTTOM_CENTER:
				// calculate y
				y = scaledHeight - 10 - yOffset;
			case TOP_CENTER:
				// calculate x (ignore offset)
				x = (scaledWidth - textRenderer.getWidth( text )) / 2;
				break;

			case BOTTOM_RIGHT:
				// calculate y
				y = scaledHeight - 10 - yOffset;
			case TOP_RIGHT:
				// calculate x
			  	x = scaledWidth - textRenderer.getWidth( text ) - 5 - xOffset;
				break;
		}

		if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
		else textRenderer.draw( matrices, text, x, y, color );
	}

	public static void drawPositionedMultiLineText( MatrixStack matrices, Text[] texts, ScreenPosition position, int xOffset, int yOffset, boolean shadow, int color )
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
          			if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
          			else textRenderer.draw( matrices, text, x, y, color );

          			y += 12;
			  	}
				break;
				
			case TOP_CENTER:
		    	for ( Text text : texts )
        		{
					x = (scaledWidth - textRenderer.getWidth( text )) / 2;

					if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
					else textRenderer.draw( matrices, text, x, y, color );

					y += 12;
        		}
		    	break;
				
			case TOP_RIGHT:
				for ( Text text : texts )
        		{
					x = scaledWidth - textRenderer.getWidth( text ) - 5 - xOffset;

					if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
					else textRenderer.draw( matrices, text, x, y, color );

					y += 12;
			  	}
				break;

			case BOTTOM_LEFT:
				y = scaledHeight - 10 - yOffset;
				for ( Text text : texts )
				{
					if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
					else textRenderer.draw( matrices, text, x, y, color );

					y -= 12;
				}
				break;

			case BOTTOM_CENTER:
				y = scaledHeight - 5 - yOffset;
				for ( Text text : texts )
				{
					x = (scaledWidth - textRenderer.getWidth( text )) / 2;

					if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
					else textRenderer.draw( matrices, text, x, y, color );

					y -= 12;
				}
			case BOTTOM_RIGHT:
				y = scaledHeight - 5 - yOffset;
				for ( Text text : texts )
				{
					x = scaledWidth - textRenderer.getWidth( text ) - 5 - xOffset;

					if ( shadow ) textRenderer.drawWithShadow( matrices, text, x, y, color );
					else textRenderer.draw( matrices, text, x, y, color );

					y -= 12;
				}
				break;
		}
	}
}