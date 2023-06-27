package me.av306.xenon.config;

import me.av306.xenon.config.feature.*;
import me.av306.xenon.config.feature.chat.MultiQuickChatGroup;
import me.av306.xenon.config.feature.chat.QuickChatGroup;
import me.av306.xenon.config.feature.movement.FullKeyboardGroup;
import me.av306.xenon.config.feature.movement.TimerGroup;
import me.av306.xenon.config.feature.render.*;
import me.lortseam.completeconfig.api.*;

@ConfigEntries( includeAll = true )
public class FeatureConfigGroup implements XenonConfigGroup
{
	@Transitive
	private final GeneralConfigGroup generalConfigGroup = new GeneralConfigGroup();

	@Transitive
	private final BlackBoxGroup blackBoxGroup = new BlackBoxGroup();

	@Transitive
	private final RedReticleGroup redReticleGroup = new RedReticleGroup();

	@Transitive
	private final CommandProcessorGroup commandProcessorGroup = new CommandProcessorGroup();

	@Transitive
	private final QuickChatGroup quickChatGroup = new QuickChatGroup();

	//@Transitive
	//private final DamageIndicatorGroup damageIndicatorGroup = new DamageIndicatorGroup();

	@Transitive
	private final FeatureListGroup featureListGroup = new FeatureListGroup();

	@Transitive
	private final FullKeyboardGroup fullKeyboardGroup = new FullKeyboardGroup();

	@Transitive
	private final TakePanoramaGroup takePanoramaGroup = new TakePanoramaGroup();

	@Transitive
	private final WailaGroup wailaGroup = new WailaGroup();

	@Transitive
	private final TimerGroup timerGroup = new TimerGroup();

	@Transitive
	private final ProximityRadarGroup proximityRadarGroup = new ProximityRadarGroup();

	@Transitive
	private final ZoomGroup zoomGroup = new ZoomGroup();

	@Transitive
	private final MultiQuickChatGroup multiQuickChatGroup = new MultiQuickChatGroup();
}