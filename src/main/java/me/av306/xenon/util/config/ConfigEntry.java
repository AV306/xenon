package me.av306.xenon.util.config;

public class ConfigEntry<T extends Object>
{
    public T configuration;

    public ConfigEntry( T value ) { this.configuration = value; }
}
