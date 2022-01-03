package me.av306.xenon.util.commands;

import me.av306.xenon.features.interfaces.ICommand;
import me.av306.xenon.features.interfaces.IFeature;

import java.util.HashMap;

public class CommandRegistry
{
    public HashMap<String, ICommand> registry = new HashMap<>();


    public CommandRegistry() {}
}
