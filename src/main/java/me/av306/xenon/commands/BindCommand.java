package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import me.av306.xenon.feature.IFeature;
import net.minecraft.client.input.Input;
import net.minecraft.client.util.InputUtil;

public class BindCommand extends Command
{
    public BindCommand()
    {
        super( "bind", "b") ;
    }

    @Override
	public void execute( String[] args )
	{
		// args[0] is feature name, args[1][0] is key
        try
        {
            IFeature feature = Xenon.INSTANCE.featureRegistry.get( args[0] );
            char key = args[1].charAt( 0 );

            // Idk what the scancodes are :(
            feature.getKeyBinding().setBoundKey( InputUtil.fromKeyCode( key, -1 ) );

            Xenon.INSTANCE.sendInfoMessage( "text.xenon.command.bind.success", args[0], key );
        }
        catch ( NullPointerException npe )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.command.bind.unresolvable", args[0] );
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.command.bind.notenoughargs", args.length );
        }
	}
}
