package me.av306.xenon.feature.template;


public abstract class IUpdatableFeature extends IToggleableFeature
{
	protected IUpdatableFeature( String name ) { super( name ); }
	protected IUpdatableFeature() { this( "IUpdatableFeature" ); }
	
	public abstract void onUpdate();
}