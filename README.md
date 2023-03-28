# Xenon: A lightweight Fabric utility client

[![Gradle CI](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml/badge.svg?branch=1.19-DEV)](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)
[![Custom badge](https://img.shields.io/endpoint?color=3fcc98&url=https%3A%2F%2Fhits.dwyl.com%2FAV306%2Fxenon.json%3Fshow%3Dunique)]()

Hello! This is Xenon, a Minecraft utility mod that adds stuff that aims to make Minecraft more pleasant and convenient.

Xenon tries to implement features similar to those in clients such as Badlion and Lunar, while maintaining compatibility with other mods. 

This project often has bugs and missing translation keys, as I unfortunately can't extensively test everything due to school and other commitments. If you find anything wrong, please raise an issue! PRs are very welcome.

A server-side opt-out companion mod for the slightly more controversial features is currently being developed.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Latest Fabric API (may not work with older ones, not really tested)
- Fabric Loader >=0.14.7

## Known incompatabilities:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client (will almost definitely break, not fully tested)
- LazyDFU * (Won't crash, but will override Xenon's optimisation setting)

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

## Features:

Full documentation [here](https://github.com/AV306/xenon/blob/1.19-DEV/FEATURES.md)

(Most features are unbound by default)

- FullBright (fullbright)
- FullKeyboard (Move around without a mouse! Touchscreen / mouse still needed for UI interaction)
- QuickChat (send a preset message with one button, commands WIP)
- MultiQuickChat (QuickChat, but with up to 10 messages)
- Panorama Generator (generate title screen panoramas that can be used in a resource pack! Note: broken)
- ShareLocation (share your location and dimension, not recommended for public survival servers, security WIP)
- Australian Mode (Aussie Minecraft, approved by an actual Australian)
- Zoom (Adjustable zoom)
- Halo "Red Reticle" (Makes your crosshair change color when pointing at hostile mobs, like its namesake in Halo games)
- Debug crosshair size modification (Make the axes easier to see!)
- Command processor ([guide](https://github.com/AV306/xenon/blob/1.19-DEV/CMD.md))

## Licensed under the MIT license

## Credits (for integrations)

- **LazyDFU**: [astei/lazydfu](https://github.com/astei/lazydfu)
