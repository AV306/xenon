package me.av306.xenon.feature;


public abstract class IUpdatableFeature extends IToggleableFeature
{
	protected IUpdatableFeature( String name ) { super( name ); }
	protected IUpdatableFeature() { this( "IUpdatableFeature" ); }

	public void update()
	{
		if ( isEnabled ) onUpdate();
	}

	public abstract void onUpdate();
}