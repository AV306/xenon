package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public abstract class IToggleableFeature extends IFeature
{
    //protected int key = GLFW.GLFW_KEY_UNKNOWN;

	protected IToggleableFeature( String name )
    {
        super( name );
    }

    /*@Override
    protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
    {
        if ( action == GLFW.GLFW_PRESS && key == this.key )
            toggle();

        return ActionResult.PASS;
    }*/

    @Override
    protected void registerKeyEvent() {
        if ( this.keyBinding.wasPressed() )
            this.toggle();
    }

    @Override
    public void enable()
    {
        if ( isEnabled ) return; // safety

        isEnabled = true;

        Xenon.INSTANCE.enabledFeatures.add( this );

        onEnable();
    }

    @Override
    public void disable()
    {
        if ( !isEnabled ) return;

        isEnabled = false;

        Xenon.INSTANCE.enabledFeatures.remove( this );

        onDisable();
    }

    public void toggle()
    {
      if ( isEnabled ) disable();
        else enable();

      Xenon.INSTANCE.client.player.sendMessage(
                new LiteralText(
                        name + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
                ).formatted( Xenon.INSTANCE.SUCCESS_FORMAT ),
                true
      );
    }
}
