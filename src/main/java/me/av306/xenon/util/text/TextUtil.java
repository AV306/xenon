package me.av306.xenon.util.text;

import me.av306.xenon.util.render.ScreenPosition;
import me.av306.xenon.Xenon;

import me.av306.xenon.util.color.ColorUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
			DrawContext context,
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

		// Default values for x and y
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

		context.drawText( textRenderer, text, x, y, color, shadow );
	}

	public static void drawPositionedText(
			DrawContext context,
			Text text,
			ScreenPosition position,
			int xOffset,
			int yOffset,
			boolean shadow,
			Formatting formatting
	)
	{
		int color = 0;

		try
		{
			color = formatting.getColorValue();
		}
		catch( NullPointerException npe )
		{
			color = ColorUtil.WHITE;
		}

		TextUtil.drawPositionedText(
				context,
				text,
				position,
				xOffset, yOffset,
				shadow,
				color
		);
	}

	public static void drawPositionedMultiLineText(
			DrawContext context,
			Text[] texts,
			ScreenPosition position,
			int xOffset, int yOffset,
			boolean shadow,
			int color
	)
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
					context.drawText( textRenderer, text, x, y, color, shadow );
          			y += 12;
			  	}
				break;
				
			case TOP_CENTER:
		    	for ( Text text : texts )
        		{
					x = (scaledWidth - textRenderer.getWidth( text )) / 2;

			        context.drawText( textRenderer, text, x, y, color, shadow );

					y += 12;
        		}
		    	break;
				
			case TOP_RIGHT:
				for ( Text text : texts )
        		{
					x = scaledWidth - textRenderer.getWidth( text ) - 5 - xOffset;

			        context.drawText( textRenderer, text, x, y, color, shadow );

					y += 12;
			  	}
				break;

			case BOTTOM_LEFT:
				y = scaledHeight - 10 - yOffset;
				for ( Text text : texts )
				{
					context.drawText( textRenderer, text, x, y, color, shadow );

					y -= 12;
				}
				break;

			case BOTTOM_CENTER:
				y = scaledHeight - 5 - yOffset;
				for ( Text text : texts )
				{
					x = (scaledWidth - textRenderer.getWidth( text )) / 2;

					context.drawText( textRenderer, text, x, y, color, shadow );

					y -= 12;
				}
			case BOTTOM_RIGHT:
				y = scaledHeight - 5 - yOffset;
				for ( Text text : texts )
				{
					x = scaledWidth - textRenderer.getWidth( text ) - 5 - xOffset;

					context.drawText( textRenderer, text, x, y, color, shadow );

					y -= 12;
				}
				break;
		}
	}

	public static void drawPositionedMultiLineText(
			DrawContext context,
			Text[] texts,
			ScreenPosition position,
			int xOffset, int yOffset,
			boolean shadow,
			Formatting formatting
	)
	{
		int color = 0;

		try
		{
			color = formatting.getColorValue();
		}
		catch( NullPointerException npe )
		{
			color = ColorUtil.WHITE;
		}

		TextUtil.drawPositionedMultiLineText(
				context,
				texts,
				position,
				xOffset, yOffset,
				shadow,
				color
		);
	}


}