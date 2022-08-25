package me.av306.xenon.util.text;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing a documentation text for Xenon.
 */
public class DocText implements TextBlock
{
    private String title;
    private List<String> contents;

    public DocText( String title, String... contents )
    {
        this.title = title;
        this.contents = Arrays.asList( contents );
    }

    @Override
    public String getTitle()
    {

    }
    
    @Override
    public List<String> getContents()
    {

    }

    @Override
    public Text[] asTextLines()
    {

    }

    @Override
    public String asMultilineString()
    {

    }
}