# Xenon

(Wow how is this getting so much traffic? Better start fixing the translations)

[![Austin CI with Gradle](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/austin_gradle.yml)
[![CodeQL](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/AV306/xenon/actions/workflows/codeql-analysis.yml)
[![HitCount](https://hits.dwyl.com/AV306/xenon.svg?style=flat&show=unique)](http://hits.dwyl.com/AV306/xenon)

Hello! This is Xenon, a Minecraft utility mod that adds stuff that makes Minecraft more pleasant and convenient.

This mod aims to implement the functionality of clients such as Badlion and Lunar, while maintaining compatability with other mods. 

This project is **not** guaranteed to be in a working, up-to-date, or complete state at *any* point, and (much) more often than not there are several translation keys missing. This is updated according to my needs since exactly 2 other people use it, and they don't really care about its completeness.

## Requirements:

(check these [here](https://fabricmc.net/develop))

- Latest Fabric API (may break if you use an older one)
- Fabric Loader >=0.13.3

## Known incompatabilities:

- WI-Zoom (breaks Xenon's FOV-related features as it overwrites `GameRenderer.getFov()`, which Xenon injects into)
- Wurst Client (will almost definitely break, not fully tested)
- LazyDFU * (Won't crash, but will optimise DFU regardless of whether you set Xenon's option)

\* LazyDFU functionality was integrated into Xenon in v4.6.1+1.19.2

## Features:

Full documentation [here](FEATURES.md)

(Most features are unbound by default)

~~Maybe someday I'll implement multi-key sequences~~

EDIT: MultiQuickChat has multi-key sequences, I did it yay

- FullBright
- QuickChat 
- MultiQuickChat
- Incomplete WAILA implementation (PRs to finish this are welcome)
- Panorama Generator
- ShareLocation
- Australian Mode
- Zoom
- Halo Red Reticle
- Debug crosshair size modification
- modular and user-friendly feature creation/registration system I spent way too much time on
- Command processor

## Licensed under GNU GPL v3

## Credits (for integrations)

- **LazyDFU**: [astei/lazydfu](https://github.com/astei/lazydfu)
