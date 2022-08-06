package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;

public class Debugger extends IToggleableFeature
{
    public Debugger()
    {
        super( "Debugger", "d", "debug" );

        this.hide = true;
    }

    @Override
    public void disable() {}

    @Override
    protected void onEnable()
    {

    }

    @Override
    protected void onDisable()
    {

    }

    @Override
    protected boolean onRequestExecuteAction( String[] action )
    {
        try
        {
            switch ( action[0] )
            {
                case "get" ->
                {

                }

                case "set" -> {

                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            Xenon.INSTANCE.sendErrorMessage( "Not enough arguments!" );
        }
        return true;
    }
}
