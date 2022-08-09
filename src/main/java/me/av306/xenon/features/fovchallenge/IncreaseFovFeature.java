package me.av306.xenon.features.fovchallenge;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IFeature;
import org.lwjgl.glfw.GLFW;

public class IncreaseFovFeature extends IFeature
{
    public IncreaseFovFeature()
    {
        super( "IncreaseFov", GLFW.GLFW_KEY_LEFT_BRACKET, "if" );
    }

    @Override
    protected void onEnable()
    {
        EventFields.FOV_MODIFIER += 10;
    }
}
