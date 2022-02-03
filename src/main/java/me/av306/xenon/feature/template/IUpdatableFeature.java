package me.av306.xenon.feature.template;


public abstract class IUpdatableFeature extends IToggleableFeature
{
	public String name = "IUpdatableFeature";
	
  public IUpdatableFeature() { super.setName( name ); }
	
	public abstract void onUpdate();
}