package me.av306.xenon.mixinterface;

// this one  has to be out here because its used for interface injection and is classloaded
// but stuff in the mixin package (whether a mixin or not) will throw an error if classloaded
// FIXME: Rename to ISimpleOption
public interface SimpleOptionAccessor<T>
{
    void forceSetValue( T newValue );
}
