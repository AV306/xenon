@echo off

REM !/bin/sh

REM Small script to quickly install optimisation / QoL mods
REM QoL mods are not downloaded by default

REM Create the mods directory if it doesn't exist
mkdir -p ./run/mods/
cd ./run/mods/


REM ModMenu
wget https://cdn.modrinth.com/data/mOgUt4GM/versions/5e62j63G/modmenu-6.1.0-rc.4.jar

REM Fabric API
REM wget https://cdn.modrinth.com/data/P7dR8mSH/versions/Pz1hLqTB/fabric-api-0.76.0%2B1.19.4.jar



REM +=======================+
REM | XENON + EXTERNAL DEPS |
REM +=======================+

REM Xenon!
REM TODO: Publish to modrinth?
REM wget

REM Complete Config (all, apparently)
REM wget https://cdn.modrinth.com/data/GtqG8z1h/versions/xTRB8xOO/completeconfig-2.3.1.jar



REM +===================+
REM | OPTIMISATION MODS |
REM +===================+

REM Sodium
wget https://cdn.modrinth.com/data/AANobbMI/versions/b4hTi3mo/sodium-fabric-mc1.19.4-0.4.10%2Bbuild.24.jar

REM Reese's Sodium Options
wget https://cdn.modrinth.com/data/Bh37bMuy/versions/aO0hSGlL/reeses_sodium_options-1.5.0%2Bmc1.19.4-build.72.jar

REM Sodium Extras
wget https://cdn.modrinth.com/data/PtjYWJkn/versions/YknbqkHe/sodium-extra-0.4.18%2Bmc1.19.4-build.100.jar

REM Lithium
wget https://cdn.modrinth.com/data/gvQqBUqZ/versions/14hWYkog/lithium-fabric-mc1.19.4-0.11.1.jar

REM Starlight
wget https://cdn.modrinth.com/data/H8CaAYZC/versions/1.1.1%2B1.19/starlight-1.1.1%2Bfabric.ae22326.jar

REM C2ME
wget https://cdn.modrinth.com/data/VSNURh3q/versions/GtUrsjth/c2me-fabric-mc1.19.4-0.2.0%2Balpha.10.30.jar

REM EntityCulling
wget https://cdn.modrinth.com/data/NNAgCjsB/versions/UvJN5Cy4/entityculling-fabric-1.6.2-mc1.19.4.jar

REM Exordium
wget https://cdn.modrinth.com/data/DynYZEae/versions/MaOM64pW/exordium-fabric-1.0.3-mc1.19.4.jar

REM ImmediatelyFast
wget https://cdn.modrinth.com/data/5ZwdcRci/versions/izX4Zjnu/ImmediatelyFast-1.1.10%2B1.19.4.jar



REM +=======================+
REM | QoL (QUALITY of LIFE) |
REM +=======================+

REM TODO