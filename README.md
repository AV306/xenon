# Xenon: A lightweight Fabric utility client

[![Gradle CI](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml/badge.svg?branch=1.19-DEV)](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml)
[![Custom badge](https://img.shields.io/endpoint?color=3fcc98&url=https://hits.dwyl.com/AV306/xenon.json?show=unique)]()

**Note: Xenon 4.0.0+1.20 will crash unless Cloth Config is installed as well**

**Note 2: I'll be taking a break from working on Xenon to concentrate on my studies; updates will happen much less frequently (not like they happen often anyway) Sorry!**


Hello! This is Xenon, a Minecraft utility mod that adds stuff that aims to make Minecraft more pleasant and convenient.

Xenon tries to implement features similar to those in clients such as Badlion and Lunar, while maintaining compatibility with other mods. 

This project often has bugs and missing translation keys, as I unfortunately can't extensively test everything due to school and other commitments. If you find anything wrong, please raise an issue! PRs are very welcome.

Servers can "opt-out" of any features they wish, by sending the following message through chat:

`{{xenon restrict [feature name]}}`, replacing `[feature name]` with any alias for the feature, e.g. `{{xenon restrict proxradar}}` for ProximityRadar.

Xenon will not display the opt-out "message", and the message will still be processed even if the client sets their interaction settings to disable chat.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Latest Fabric API (may not work with older ones, not really tested)
- Fabric Loader >=0.14.21
- CompleteConfig 2.4.x

Version numbers are fairly strict, if it says "1.19.4" that means it's only been tested on 1.19.4, but it will probably work on other 1.19.*x* versions, and the `fabric.mod.json` won't stop you.

You can even use Xenon without CompleteConfig, but configs might not work properly.

## Known Issues:

- ProximityRadar entity highlights lag behind entity movements a little
- ProximityRadar entity tracer doesn't stay attached to camera centre (moves around during view bobbing)
- FullKeyboard attack/break key double-binding does not work for attacking
- Help command doesn't do anything
- Red Reticle non-functional in 1.20

## Known incompatibilities:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client (will almost definitely break, not fully tested)
- LazyDFU * (Won't crash, but will override Xenon's optimisation setting)
- Zoomify (Thanks @catboycrimes!)

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

## Features:

Full documentation [here](FEATURES.md)

(Most features are unbound by default)

- FullBright (fullbright)
- FullKeyboard (Move around without a mouse! Touchscreen / mouse still needed for UI interaction)
- QuickChat (send a preset message with one button, commands WIP)
- MultiQuickChat (QuickChat, but with up to 10 messages)
- Panorama Generator (generate title screen panoramas that can be used in a resource pack!)
- ShareLocation (share your location and dimension, not recommended for public survival servers, security WIP)
- Australian Mode (Aussie Minecraft, approved by an actual Australian)
- Zoom (Adjustable zoom)
- Halo "Red Reticle" (Broken in 1.20) (Makes your crosshair change color when pointing at hostile mobs, like its namesake in Halo games)
- Debug crosshair size modification (Make the axes easier to see!)
- Command processor ([guide](CMD.md))

## Licensed under the MIT license

## Credits (for integrations)

- **LazyDFU**: [astei/lazydfu](https://github.com/astei/lazydfu)
