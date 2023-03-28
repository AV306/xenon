# Xenon

(Wow how is this getting so much traffic? Better start fixing the translations)

[![Gradle CI](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml/badge.svg?branch=1.19-DEV)](https://github.com/AV306/xenon/actions/workflows/gradle_ci.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)
[![HitCount](https://hits.dwyl.com/AV306/xenon.svg?style=flat&show=unique)](http://hits.dwyl.com/AV306/xenon)

Hello! This is Xenon, a Minecraft utility mod that adds stuff that aims to make Minecraft more pleasant and convenient.

This mod tries to implement the functionality of clients such as Badlion and Lunar, while maintaining compatability with other mods. 

This project is unfortunately **not** guaranteed to be in a working, up-to-date, or complete state at *any* point, especially translations. This is only updated according to my needs since exactly 1 other person uses it, and they don't really care about its completeness.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Latest Fabric API (may not work with older ones)
- Fabric Loader >=0.13.3

## Known incompatabilities:

- WI-Zoom (breaks Xenon's FOV-related features)
- Wurst Client (will almost definitely break, not fully tested)
- LazyDFU * (Won't crash, but will optimise DFU regardless of whether you set Xenon's option)

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

## Features:

Full documentation [here](FEATURES.md)

(Most features are unbound by default)

- FullBright (fullbright)
- QuickChat (send a preset message with one button)
- MultiQuickChat (QuickChat, but with up to 10 messages)
- Incomplete WAILA implementation (PRs to finish this are welcome)
- Panorama Generator (generate title screen panoramas that can be used in a resource pack!)
- ShareLocation (share your location and dimension, not recommended for public survival servers)
- Australian Mode (Aussie Minecraft)
- Zoom (Adjustable zoom)
- Halo "Red Reticle" (Makes your crosshair change color when pointing at hostile movs)
- Debug crosshair size modification (~~bug~~ _feature_)
- modular and user-friendly feature creation/registration system I spent way too much time on
- Command processor

## Licensed under MIT license

## Credits (for integrations)

- **LazyDFU**: [astei/lazydfu](https://github.com/astei/lazydfu)
