package me.av306.xenon.config;

import me.av306.xenon.config.feature.*;
import me.lortseam.completeconfig.api.*;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;

@ConfigEntries
public class FeatureConfigGroup implements XenonConfigGroup
{
	@Transitive
	private final QuickChatGroup autoreply = new QuickChatGroup();

	@Transitive
	private final FeatureListGroup featurelist = new FeatureListGroup();

	@Transitive
	private final TakePanoramaGroup panorama = new TakePanoramaGroup();

	@Transitive
	private final WailaGroup waila = new WailaGroup();

	@Transitive
	private final TimerGroup timer = new TimerGroup();
}