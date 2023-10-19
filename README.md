# Xenon: A lightweight Fabric utility client

[![Gradle CI](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml/badge.svg?branch=1.20-DEV)](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml)
[![Hits](https://img.shields.io/endpoint?color=3fcc98&url=https://hits.dwyl.com/AV306/xenon.json?show=unique)]()

Hi, thanks for stopping by this page! This is Xenon, a personal project of mine that I decided to publish to Modrinth and a couple of people decided to use.

<br>

### What does Xenon do?

Xenon tries to implement features similar to those in clients such as Badlion and Lunar, while maintaining compatibility with other mods. 

Most mods follow the traditional "problem-solution" model - they give you a solution to a common problem.

Xenon, however, is more like a Swiss Army chainsaw (but nowhere near as powerful) - it gives you a bunch of solutions and lets you figure out what to use them for. This is a side effect of the original reason I made Xenon, which was to solve weird, niche and/or pedantic problems I had.

<br>

### What "solutions" does Xenon have?

Check them all out [here!](docs/FEATURES.md)

(Most features are unbound by default)

A sample:

- FullBright (fullbright: removes lighting effects, making everything visible, even at night)
- HealthDisplay (Displays mobs' names and health in name tags)
- QuickChat (send a preset message with one button, commands WIP)
- MultiQuickChat (QuickChat, but with up to 10 messages)
- Panorama Generator (make panoramas for resource packs! Incompatible with shaders unfortunately)
- ShareLocation (share your location and dimension, not recommended for public survival servers, security WIP)
- Australian Mode (Aussie Minecraft, approved by an Australian)
- Zoom (Zoom, adjustable with the scroll wheel)
- Halo "Red Reticle" (Makes your crosshair change color when pointing at hostile mobs, like in Halo games)
- Debug crosshair size modification (Make the axes easier to see!)

<br>

### Requirements:

(check these [here](https://fabricmc.net/develop))

- [Latest Fabric API](https://modrinth.com/mod/fabric-api) (may not work with older ones, not really tested)
- Fabric Loader >=0.14.21
- [CompleteConfig](https://modrinth.com/mod/completeconfig) 2.4.x

Version numbers are fairly strict, if it says "1.19.4" that means it's only been tested on 1.19.4, but it will probably work on other 1.19.*x* versions, and no one will stop you.

<br>

### A note on version numbers

The current latest version is **4.2.0** for 1.20, 1.20.1 and 1.20.2. You might still see references to versions that appear "greater" than this (e.g. LDFU intg in 4.6.1), because I only started following proper SemVer around the time the 1.19.4/1.20 versions were released.

Just get the greatest version number for your version and ignore those tagged "1.19" (e.g. "4.6.0+1.19" is *very* outdated compared to "4.1.0+1.19.4") and you should be fine :)

<br>

### Warning

This project often has bugs and missing translation keys, as I unfortunately can't extensively test everything due to school and other commitments. If you find anything wrong, please raise an issue! PRs are very welcome.

<br>

### Known Issues:

- ProximityRadar entity highlights lag behind entity movements a little (wontfix)
- ProximityRadar entity tracer doesn't stay attached to camera centre (moves around during view bobbing) (might fix someday)
- FullKeyboard attack/break key double-binding does not work for attacking (please send help)
- Help command doesn't do anything ( :( )

<br>

### Known incompatibilities:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client (will almost definitely break, not fully tested)
- LazyDFU * (Won't crash, but will override Xenon's optimisation setting)
- Zoomify (Thanks @catboycrimes!)

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

<br>

### Server opt-out

Servers can "opt-out" of any features they wish, by sending the following message through chat:

`{{xenon restrict [feature name]}}`, replacing `[feature name]` with any alias for the feature, e.g. `{{xenon restrict proxradar}}` for ProximityRadar.

Xenon will not display the opt-out "message", and the message will still be processed even if the client sets their interaction settings to disable chat.

<br>

## Credits (for integrations)

- **LazyDFU**: [astei/lazydfu](https://github.com/astei/lazydfu)
