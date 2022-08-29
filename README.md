# Xenon

[![Austin CI with Gradle](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)
[![HitCount](https://hits.dwyl.com/AV306/xenon.svg?style=flat&show=unique)](http://hits.dwyl.com/AV306/xenon)

Hello! This is Xenon, a Minecraft utility mod that adds stuff that makes Minecraft more pleasant and convenient.

This mod aims to implement the functionality of clients such as Badlion and Lunar, while maintaining compatability with other mods. 

This project is **not** guaranteed to be in a working, up-to-date, or complete state at *any* point, and (much) more often than not there are several translation keys missing. This is updated according to my needs since exactly 2 other people use it, and they don't really care about the feature versions.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Latest Fabric API (may break if you use an older one)
- Fabric Loader >=0.13.3

## Incompatible mods:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client (will almost defnintely break, not fully tested)
- LazyDFU * (Won't crash, but will override your configuration of whether Xenon should disable DFU "optimisations")
- Please help find some more!

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

## Features:

Full documentation [here](FEATURES.md)

(All are unbound by default)

~~Maybe someday I'll implement multi-key sequences~~

EDIT: MultiQuickChat has multi-key sequences, I did it yay

- FullBright (Allows you to quickly set gamma to 100.0. No more pitch-black caves!)
- QuickChat (Quickly send a predefined message)
- MultiQuickChat (Quickly send one of several predefined messages)
- Bad WAILA implementation (use WTHIT instead, it's much better)
- Panorama Generator (Allows you to take panoramas. I'm proud of this one) (Note: does not work with shaders)
- ShareLocation (Quickly share your location with others! Under maintenance)
- FeatureList (not really a feature)
- Configuration system
- Timer (Speeds everything up! *wheee*)
- Australian Mode (Turn the world upside down!)
- Zoom (Look at far away things! WI-Zoom like scroll-zoom function that works with Xenon's other fov-related features)
- modular and user-friendly feature creation/registration system I spent way too much time on (yay!)
- Command processor and the beginnings of a macro system (Too much code in there)

## Licensed under GNU GPL v3

## IFAQ (Infrequently Asked Questions)
Q: How do I use this?

A: Download a build that matches your Minecraft version, and drop it in your mods folder.
Google "How to install Fabric mods" for more info.


Q: Is there a Forge version?

A: No, and probably never. It's very hard to find good Forge API documentation, and Fabric is, in my slightly biased opinion, better overall.

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
5. Add any translation keys to the lang file.
