package me.av306.xenon.config;

import me.av306.xenon.config.feature.*;
import me.lortseam.completeconfig.api.*;

@ConfigEntries( includeAll = true )
public class FeatureConfigGroup implements XenonConfigGroup
{
	@Transitive
	private final GeneralConfigGroup generalConfigGroup = new GeneralConfigGroup();

	@Transitive
	private final CommandProcessorGroup commandProcessorGroup = new CommandProcessorGroup();

	@Transitive
	private final QuickChatGroup quickChatGroup = new QuickChatGroup();

	@Transitive
	private final FeatureListGroup featureListGroup = new FeatureListGroup();

	@Transitive
	private final TakePanoramaGroup takePanoramaGroup = new TakePanoramaGroup();

	@Transitive
	private final WailaGroup wailaGroup = new WailaGroup();

	@Transitive
	private final TimerGroup timerGroup = new TimerGroup();

	@Transitive
	private final ProximityRadarGroup proximityRadarGroup = new ProximityRadarGroup();

	@Transitive
	private final FastBreakGroup fastBreakGroup = new FastBreakGroup();

	@Transitive
	private final HighJumpGroup highJumpGroup = new HighJumpGroup();

	@Transitive
	private final CreativeFlightGroup creativeFlightGroup = new CreativeFlightGroup();

	@Transitive
	private final JetpackGroup jetpackGroup = new JetpackGroup();

	@Transitive
	private final JumpBoostGroup jumpBoostGroup = new JumpBoostGroup();

	@Transitive
	private final ZoomGroup zoomGroup = new ZoomGroup();

	@Transitive
	private final MultiQuickChatGroup multiQuickChatGroup = new MultiQuickChatGroup();

	//@Transitive
	//private final ExtraReachGroup extraReachGroup = new ExtraReachGroup();
}