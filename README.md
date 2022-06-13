# Xenon

[![Austin CI with Gradle](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)

Hello! This is Xenon, a Minecraft utility mod that adds stuff like Fullbright and WAILA functionality to Minecraft.

This is good for people who want the features of clients such as Badlion or Lunar, but want it to be compatible with other mods.

NOTE: Once a new Minecraft version is released, support for previous versions tends to be dropped due to my bad version control habits.

## Requirements:

- Fabric Loader >= 0.13.3 (Recommended: 0.13.3 Just use the latest version, it's better that way)
- Fabric API for that version (Recommended: 0.51.0+1.18.2)

## Incompatible mods:

- Nexus

## Features:

(All are unbound by default)
~~Maybe someday I'll implement multi-key seqnences~~
- FullBright 
- QuickChat
- Bad WAILA implementation (use WTHIT instead)
- Panorama Generator (I'm proud of this one)
- ShareLocation
- NoFireOverlay (useless, present in code but not accessible, exists for initial testing purposes)
- FeatureList (not really a feature)
- DataHUD (useless)
- Configuration system
- ProximityRadar
- Timer (*wheee*)
- FastBreak (Does not work *exactly* as intended, but close enough)
- Fundy Ice Physics™ (WIP)
- SpeedBoost (WIP)
- HighJump (WIP)
- AntiKnockback (WIP)
- modular and user-friendly feature creation/registration system I spent way too much time on

Planned features:

- AntiAFK
- QuickCommand
- FreeCam

## Licensed under GNU GPL v3

## Instructions for developers

### Setup

(If you just want to build the latest version, run ./scripts/\[os\]/export; e.g. ./scripts/nix/export for Linux/MacOS)

1. Install JDK 17 or above (I compiled 4.1 with JDK 18, 17 might not work)
2. Run ./gradlew eclipse if using Eclipse, otherwise IntelliJ *should* automatically set everything up.
3. Open the root folder as a project.
4. Done!

### Adding new features

1. Extend IFeature or IToggleableFeature depending on the type of feature.
2. Define a constructor and call `super( <name> );`, replacing `<name>` with the name of your feature.
3. Register event callbacks in the constructor, see `Timer` for an example. (Note: some features, like JumpBoost, that require  modifying the values of fields right before they are accessed, are more complex. I'm working on that.)
4. (**IMPORTANT**) Register your feature by calling its constructor in `Xenon` (main class).
