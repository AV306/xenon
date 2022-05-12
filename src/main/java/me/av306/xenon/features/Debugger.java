package me.av306.xenon.features;

import me.av306.xenon.feature.IFeature;
import org.lwjgl.glfw.GLFW;

public class Debugger extends IFeature
{
    public Debugger()
    {

        super( "Debugger", GLFW.GLFW_KEY_BACKSLASH );
        //KeyEvent.AFTER_VIGNETTE.register( this::onKeyboardKey );
    }

    @Override
    public void onEnable()
    {

    }

    /*protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
    {
        if ( action == GLFW.GLFW_PRESS && GLFW.glfwGetKey( window, this.key ) == GLFW.GLFW_PRESS )
        {
            switch ( key )
            {
                case GLFW.GLFW_KEY_1:
                    // save & reload config
                    Xenon.INSTANCE.sendInfoMessage( "text.xenon.debug.configreload" );
                    Xenon.INSTANCE.config.save();
                    Xenon.INSTANCE.config.load();
                    break;

                case GLFW.GLFW_KEY_2:
                    Xenon.INSTANCE.sendInfoMessage( "text" );
                    break;

                default:
                    break;

            }
        }

        return ActionResult.PASS;
    }*/
}
