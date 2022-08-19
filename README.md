# Xenon

[![Austin CI with Gradle](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)
[![HitCount](https://hits.dwyl.com/AV306/xenon.svg?style=flat&show=unique)](http://hits.dwyl.com/AV306/xenon)

Hello! This is Xenon, a Minecraft utility mod that adds stuff like Fullbright and WAILA functionality to Minecraft.

This is good for people who want the features of clients such as Badlion or Lunar, but want it to be compatible with other mods.

NOTE: Once a new Minecraft version is released, support for previous versions tends to be dropped due to my bad version control habits. Also, I randomly skip versions because this is built according to my needs, and almost no one uses this.

This project is **not** guaranteed to be in a working or complete state at *any* point, and (much) more often than not there are several translation keys missing.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Fabric Loader >= 0.13.3 (Recommended: 0.13.3 Just use the latest version, it's better that way)
- Fabric API for that version (Recommended: 0.57.0+1.19)

## Incompatible mods:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client
- Please help find some more!

## Features:

(All are unbound by default)

~~Maybe someday I'll implement multi-key sequences~~

EDIT: MultiQuickChat has multi-key sequences, I did it yay

- FullBright (Allows you to quickly set gamma to 100.0. No more pitch-black caves!)
- QuickChat (Quickly send a predefined message)
- MultiQuickChat (Quickly send one of several predefined messages)
- Bad WAILA implementation (use WTHIT instead, it's much better)
- Panorama Generator (Allows you to take panoramas. I'm proud of this one) (Note: does not work with shaders)
- ShareLocation (Quickly share your location with others! Under maintenance)
- NoFireOverlay (useless, present in code but not accessible, exists for initial testing purposes)
- FeatureList (not really a feature)
- DataHUD (useless)
- Configuration system
- ProximityRadar (Tells you quietly when a hostile mob or projectile is near)
- Timer (Speeds everything up! *wheee*)
- FastBreak (Speeds up block breaking. Does not work *exactly* as intended, but close enough)
- Fundy Ice Physics™ (Sort of work*ed*, I forgot what happened to it)
- SpeedBoost (WIP)
- HighJump (Jump high!)
- JumpBoost (Jump high, all the time)
- NoFallDamage (Don't die when you fall down! P.s. if you enable it mid-air, you might die. Of fall damage.)
- Australian Mode (Turn the world upside down!)
- Zoom (Look at far away things! WI-Zoom like scroll-zoom function that works with Xenon's other fov-related features)
- modular and user-friendly feature creation/registration system I spent way too much time on (yay!)
- Command processor and the beginnings of a macro system (Too much code in there)

## Licensed under GNU GPL v3

## Instructions for developers

### Setup

(If you just want to build the latest version, run ./scripts/\[os\]/export; e.g. ./scripts/nix/export for Linux/MacOS)

1. Install JDK 17 ~~or above~~ (Minecraft's bundled JRE is version 17, and it won't work if you compile it with JDK 18)
2. Run ./gradlew eclipse if using Eclipse, otherwise IntelliJ *should* automatically set everything up.
3. Open the root folder as a project.
4. Done!

### Adding new features

1. Extend IFeature or IToggleableFeature depending on the type of feature.
2. Define a constructor and call `super( <name> );`, replacing `<name>` with the name of your feature.
3. Register event callbacks in the constructor, see `Timer` for an example. (Note: some features, like JumpBoost, that require  modifying the return values of methods, are more complex. Use `EventFields`, see `JumpBoost`. Please DO NOT just write your code in the mixin itself, that defeats the whole purpose of the event system >:( )
4. Register your feature by calling its constructor in `Xenon` (main class).
5. Add any translations to the lang file.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

.

why did i even spend more than a year of my life making this, no one even cares about or notices, much less uses this stupid thing

update: someone noticed yayy
