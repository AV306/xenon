package me.av306.xenon.util.update;

import kong.unirest.Unirest;
import me.av306.xenon.Xenon;

public class UpdateChecker
{
    public static boolean checkForUpdate( String url )
    {
         /*ient = HttpClient.newBuilder()
                .version( Version.HTTP_2 ) // HTTP ver 2
                .followRedirects( Redirect.NORMAL ) // follow redirects normally
                .connectTimeout( Duration.ofSecond( 30 ) ) // wait 30s then timeout
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri( uri )*/

        // retrieve the latest tag (should be at the top)
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

        return latestVersion > currentVersion;
    }
}