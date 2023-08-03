package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.KeybindUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
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

            feature.setKeyBinding( KeybindUtil.registerKeybind(
				            "key.xenon." + feature.getName()
                                    .toLowerCase()
                                    .replaceAll( " ", "" ),
				            key,
				            "category.xenon." + feature.getCategory()
		            )
            );

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
