package me.av306.xenon.util.text;

import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.Xenon;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtil
{
	// TODO: add more stuff
	public static void drawPositionedText( MatrixStack matrices, Text text, ScreenPosition position, int color )
	{
		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;
		int scaledWidth = Xenon.INSTANCE.client.getWindow().getScaledWidth();
		int scaledHeight = Xenon.INSTANCE.client.getWindow().getScaledHeight();
		int x = 0;
		switch ( position )
		{
			case TOP_LEFT:
				textRenderer.drawWithShadow( matrices, text, 5, 5, color );
				break;
				
			case TOP_CENTER:
				// calculate x
				x = (scaledWidth - textRenderer.getWidth( text )) / 2;
				textRenderer.drawWithShadow( matrices, text, 5, x, color );
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
	}

	public static void drawPositionedMultiLineText( MatrixStack matrices, Text[] texts, ScreenPosition position, int yOffset, int color )
	{
		TextRenderer textRenderer = Xenon.INSTANCE.client.textRenderer;
		int scaledWidth = Xenon.INSTANCE.client.getWindow().getScaledWidth();
		int scaledHeight = Xenon.INSTANCE.client.getWindow().getScaledHeight();
		int y = 0;
		switch ( position )
		{
			case TOP_LEFT:
				y = 5 + yOffset;
        for ( Text text : texts )
        {
          textRenderer.drawWithShadow( matrices, text, 5, y, color );

          y += 12;
			  }
				break;
				
			case TOP_CENTER:
		    // calculate y
		    y = 5 + yOffset;
				
		    for ( Text text : texts )
        {
					int x = (scaledWidth - textRenderer.getWidth( text )) / 2;
					
          textRenderer.drawWithShadow( matrices, text, x, y, color );

          y += 12;
			  }
				break;
				
			case TOP_RIGHT:
				// calculate x and y
				for ( Text text : texts )
        {
					int x = scaledWidth - textRenderer.getWidth( text );
					
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