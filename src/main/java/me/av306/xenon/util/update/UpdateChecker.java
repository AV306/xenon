package me.av306.xenon.util.update;

import me.av306.xenon.Xenon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class UpdateChecker
{

    public static String getLatestVersion( String urlString )
    {
        String latestVerString = "0.0.0";
        try
        {
            URL api = new URL( urlString );

            HttpURLConnection connection = (HttpURLConnection) api.openConnection(); // IOException
            connection.setRequestMethod( "GET" );
            connection.connect(); // SocketTimeoutException or IOException

            // response code!
            int responseCode = connection.getResponseCode();

            if ( responseCode != 200 )
                Xenon.INSTANCE.LOGGER.warn( "Oh no! Update check returned code {} and this is very bad!!!", responseCode );
            else
            {
                // we are connecting to my intermediary server,
                // which will extract the latest tag.
                // It will return the latest version as a string,
                // which we then convert to an int later.
                // E.g. "4.3.1+1.19.x" -> 431
                // "4.4.0+1.19.x" -> 440
                latestVerString = new BufferedReader( new InputStreamReader( connection.getInputStream() ) ); // I lost count alr
            }
        }
        catch ( Exception e )
        {
            Xenon.INSTANCE.LOGGER.warn( e );
            latestVerString = "0.0.0"; // safety
        }

        Xenon.INSTANCE.LOGGER.info( "Latest version string: {}", latestVerString );

        return latestVerString;

        /*
        String latestTag;
        try
        {
            latestTag = Unirest.get( url )
                .asJson()
                .getArray()
                .getJSONObject( 0 )
                .getString( "name" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            latestTag = "0.0.0";
        }

        // parse the tag to a number
        int latestVersion = 0;
        int currentVersion = 0;
        try
        {
            // e.g. "4.4.0+1.19.x" -> "4.4.0" -> "440" -> 440
            latestVersion = Integer.parseInt( latestTag.split( "+" )[0].replaceAll( ".", "" ) );

            currentVersion = Integer.parseInt( Xenon.INSTANCE.version.replaceAll( ".", "" ) );
        }
        catch ( NumberFormatExcepton nfe )
        {
            latestVersion = 0;
            currentVersion = 0;
        }

        return latestVersion > currentVersion;*/
    }

    public static int getVersionInt( String verString )
    {
        int verInt = 0;

        try
        {
            // "4.4.0" -> "440" -> 440
            verInt = Integer.parseInt( verString.replaceAll( "\\.", "" ).trim() );
        }
        catch( NumberFormatException nfe )
        {
            Xenon.INSTANCE.LOGGER.warn( "Invalid version string {}!", verString );
            verInt = 0;
        }

        return verInt;
    }

    /*public static int getCurrentVersionInt()
    {
        int temp = 0;

        try
        {
            // "4.3.1" -> "431" -> 431
            temp = Integer.parseInt( Xenon.INSTANCE.getVersion().replaceAll("\\.", "" ).trim() );
            Xenon.INSTANCE.LOGGER.info( "Current version: {}", temp );
        }
        catch ( NumberFormatException nfe )
        {
            Xenon.INSTANCE.LOGGER.warn( nfe );
        }

        return temp;
    }*/
}
