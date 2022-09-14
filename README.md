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

- FullBright
- QuickChat 
- MultiQuickChat
- Bad WAILA implementation
- Panorama Generator
- ShareLocation
- FeatureList
- Configuration system
- Australian Mode
- Zoom
- Halo Red Reticle
- Crosshair size modification (untested)
- modular and user-friendly feature creation/registration system I spent way too much time on
- Command processor and the beginnings of a macro system

## Licensed under GNU GPL v3

## IFAQ (Infrequently Asked Questions)
Q: How do I use this?

A: Download a build that matches your Minecraft version, and drop it in your mods folder.
Google "How to install Fabric mods" for more info.
