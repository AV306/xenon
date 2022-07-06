package me.av306.xenon.util.update;

import me.av306.xenon.Xenon;

import java.net.*;

public class UpdateChecker
{
    public static boolean checkForUpdate( String urlString )
    {
        return getLatestVersion( urlString ) > getCurrentVersion();
    }

    private static int getLatestVersion( String urlString )
    {
        int latestVer = 0;
        try
        {
            URL api = new URL( urlString );

            HttpUrlConnection connection = (HttpUrlConnection) api.openConnection(); // IOException
            connection.setRequestMethod( "GET" );
            connection.connect(); // SocketTimeoutException or IOException

            // response code!
            int responseCode = connection.getResponseCode();

            if ( responseCode != 200 )
                Xenon.INSTANCE.LOGGER.warn( "Oh no! Update check returned code {} and this is very bad!!!", responseCode );
            else
            {
                // we are connecting to my intermediary server,
                // which will perform the backend bookkeeping on the tags.
                // It will return the latest version as a integer.
                // E.g. "4.3.1+1.19.x" -> 431
                // "4.4.0+1.19.x" -> 440
                // These can then be compared to the current version easily.
                latestVer = (int) connection.getContent(); // CCE
            }
        }
        catch ( Exception e )
        {
            Xenon.INSTANCE.LOGGER.warn( e );
        }

        return latestVer;

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

    private static int getCurrentVersion()
    {
        int temp = 0;

        try
        {
            // "4.3.1" -> "431" -> 431
            temp = Integer.parseInt( Xenon.INSTANCE.version.replaceAll( "." ).trim() );
        }
        catch ( NumberFormatException nfe )
        {
            Xenon.INSTANCE.warn( nfe );
            temp = 0;
        }

        return temp;
    }
}