package me.av306.xenon.util.gui;

import me.av306.xenon.Xenon;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;

public class XenonInfoOverlay extends Overlay
{
    public XenonInfoOverlay()
    {

    }


    @Override
    public void render( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        super.drawCenteredText( matrices, Xenon.INSTANCE.CLIENT.textRenderer, "HALLOOOOOOOOO", 0, 0, 3 );
    }


    @Override
    public boolean pausesGame()
    {
        return false;
    }
}
