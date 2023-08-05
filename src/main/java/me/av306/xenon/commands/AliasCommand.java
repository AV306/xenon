package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.KeybindUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class AliasCommand extends Command
{
    public AliasCommand()
    {
        super( "alias" );
    }

    @Override
	public void execute( String[] args )
	{
		// args[0] is feature name, args[1] is new alias
        try
        {
            IFeature feature = Xenon.INSTANCE.featureRegistry.get( args[0] );
            String alias = args[1];

            Xenon.INSTANCE.featureRegistry.put( alias, feature );

            this.sendInfoMessage( "text.xenon.command.alias.success", args[0] );
        }
        catch ( NullPointerException npe )
        {
            this.sendErrorMessage( "text.xenon.command.unresolvable", this.name, args[0] );
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            this.sendErrorMessage( "text.xenon.command.notenoughargs", this.name, args.length, 2 );
        }
	}
}
