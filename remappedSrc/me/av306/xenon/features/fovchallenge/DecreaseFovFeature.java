package me.av306.xenon.features.fovchallenge;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IFeature;
import org.lwjgl.glfw.GLFW;

public class DecreaseFovFeature extends IFeature
{
    public DecreaseFovFeature()
    {
        super( "DecreaseFov", GLFW.GLFW_KEY_RIGHT_BRACKET, "df" );
    }

    @Override
    protected void onEnable()
    {
        EventFields.FOV_MODIFIER -= 10;
    }
}
