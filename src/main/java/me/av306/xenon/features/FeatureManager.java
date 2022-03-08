package me.av306.xenon.features;

import me.av306.xenon.feature.*;

import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Xenon Feature Manager.
 * Does stuff like have a HashMap for conversion of Feature name strings to classes.
 * Also maintains an ArrayList of enabled features.
 * Exposes most of these through instance methods and fields.
 */  
public class FeatureManager
{
	private HashBiMap<String, IFeature> featureMap = HashBiMap.create();
	public HashBiMap<String, IFeature> getFeatureMap() { return this.featureMap; }

	private ArrayList<String> enabledFeatures = new ArrayList<>();
	public ArrayList<String> getEnabledFeatures() { return this.enabledFeatures; }
	
  public FeatureManager() {}

	/**
	 * Returns null if there is no feature wit that name.
	 */
	public @Nullable IFeature getFeatureByName( @NotNull String name )
	{
		if ( featureMap.containsKey( name ) ) return featureMap.get( name );
		else return null;
	}

	public @Nullable String getNameOfFeature( @NotNull IFeature feature )
	{
		if ( featureMap.inverse().containsKey( feature ) ) 
			return featureMap.inverse().get( feature );
		else return null;
	}
}
