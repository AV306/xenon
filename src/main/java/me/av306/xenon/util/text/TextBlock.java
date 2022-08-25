package me.av306.xenon.util.text;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing a block of text in Xenon.
 * May or may not be multiline.
 */
public interface TextBLock
{
    public String getTitle();
    public List<String> getContents();

    public Text[] asTextLines();

    public String asMultilineString();

    public String[] asStringLines();
}