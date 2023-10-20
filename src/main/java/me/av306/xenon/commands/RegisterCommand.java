package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;

public class RegisterCommand extends Command
{
    public RegisterCommand() { super( "register", "reg" ); }

    @Override
    public void execute( String[] args )
    {
        try
        {
            Class<?> c = Class.forName( args[0] );

            c.getDeclaredConstructor().newInstance();
        }
        catch ( NullPointerException npe )
        {
            this.sendErrorMessage( "text.xenon.command.notenoughargs", args.length, 1 );
        }
        catch ( ClassNotFoundException cnfe )
        {
            this.sendErrorMessage( "text.xenon.command.register.classnotfound", args[0] );
        }
        catch ( Exception e )
        {
            this.sendErrorMessage( "text.xenon.register.nosuchmethod", args[0] );
        }
    }
}
