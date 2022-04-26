package me.av306.xenon.config.feature;

import me.lortseam.completeconfig.api.*;
import me.lortseam.completeconfig.ConfigContainer.Transitive;

@ConfigEntries
public class FeatureConfigGroup implements ConfigGroup
{
	@Transitive
	private final AutoReplyGroup autoreply = new AutoReplyGroup();

	@Transitive
	private final FeatureListGroup featurelist = new FeatureListGroup();

	@Transitive
	private final TakePanoramaGroup panorama = new TakePanoramaGroup();

	@Transitive
	private final WailaGroup waila = new WailaGroup();
}