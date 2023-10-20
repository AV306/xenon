# Xenon Feature Documentation

All are open for suggestions and PRs!

<br>

## Chat

### QuickChat

Send a custom message at the push of a button!

Note: commands will be sent as a chat message, fix for that is WIP

| **Option** | **Max/Min** | **Default Value** |
|------------|-------------|-------------------|
| Message    | NA          | (Blank)           |

<br>

### MultiQuickChat

Send one of 10 custom message at the push of two buttons - the feature key and a number key

| **Option** | **Max/Min** | **Default Value** |
|------------|-------------|-------------------|
| Message 0  | NA          | (Blank)           |
| Message 1  | NA          | (Blank)           |
| Message 2  | NA          | (Blank)           |
| Message 3  | NA          | (Blank)           |
| Message 4  | NA          | (Blank)           |
| Message 5  | NA          | (Blank)           |
| Message 6  | NA          | (Blank)           |
| Message 7  | NA          | (Blank)           |
| Message 8  | NA          | (Blank)           |
| Message 9  | NA          | (Blank)           |

<br>

### ShareLocation

Send a chat message with your current location and dimension, also at the push of a button. (WIP: /whisper support for public servers)

<br>

### Infinite Chat Length

Remove the maximum chat length restriction.

<br>

## Movement

### Timer

Speed up your actions! May be considered a hack on some servers, but still genuinely useful where it is allowed. USE AT YOUR OWN RISK; server opt-out available.

| **Option**          | **Max/Min** | **Default Value** | **Comment**              |
|---------------------|-------------|-------------------|--------------------------|
| Speed               | 0.1/50.0    | 1.0               | TPS multiplier           |
| Adjustment interval | 0.1/10.0    | 1.0               | Scroll adjustment amount |

**Server opt-out:** `{{xenon restrict timer}}` (I forgor how the restriction system works so this might not work)

<br>

### FullKeyboard

Play Minecraft using only your keyboard!
Disclaimer: Touchscreen still required for some UI interaction (ugh); MidnightControls is *much* better.

(This feature was designed so that I can play Minecraft on my school iPad over VNC)

(it is a bit broken)

<br>

## Render

### Australian Mode

Joke feature: simulates Australian Minecraft (upside down)

<img src="https://cdn.modrinth.com/data/BsmAXLQn/images/3a851f012826d957e0e89da4f060940868c300c7.png" style="width: 600px;"></img>

<br>

### FullBright

Full gamma, making every surface clearly visible without lighting effects (incompatible with most shaders)

<br>

### ProximityRadar

Outlines nearby players and hostile mobs, optionally through walls. May be considered a hack. (Server opt-out available)

| **Option**          | **Max/Min**        | **Default Value** | **Comment**                                                                               |
|---------------------|--------------------|-------------------|-------------------------------------------------------------------------------------------|
| Player Range        | 1 <-> 500 (blocks) | 20                | Player scan radius                                                                        |
| Show Player Box     | NA                 | True              | Draw a box around players                                                                 |
| Show Player Tracer. | NA                 | True              | Draw a line from the centre of your screen to players                                     |
| Player Box Color    | NA                 | `#ffffff` (white) | Color of the player highlight box and tracer. Use a hex color picker to find color values |
| Hostile Range       | 1 <-> 500 (blocks) | 20                | Player scan radius                                                                        |
| Show Hostile Box    | NA                 | True              | Draw a box around hostile mobs                                                            |
| Show Hostile Tracer | NA                 | True              | Draw a line from the centre of your screen to hostile mobs                                |
| Hostile Box Color   | NA                 | `#ff0000` (red)   | Color of hostile entity highlight box and tracer                                          |
| Item Range          | 1 <-> 500 (blocks) | 20                | Item scan radius                                                                          |
| Show Hostile Box    | NA                 | True              | Draw a box around items                                                                   |
| Show Hostile Tracer | NA                 | True              | Draw a line from the centre of your screen to items                                       |
| Item Box Color      | NA                 | `#74becf` (aqua)  | Color of item highlight box and tracer                                                    |
    
<img src="https://cdn.modrinth.com/data/BsmAXLQn/images/1a57e4c1cf763620f5bc06102d807b33485cbdd6.png" style="width: 600px;" />


<br>

### WAILA

What block is that?

Sub-par WAILA implementation; you're better off using another mod like WTHIT. This is just here for fun.

(This used to show entity health, but that was replaced by HealthDisplay. About the only special element here is that its range can be extended pretty far.)

<img src="img/5B80582F-AF6F-4A64-8F23-183AA096ADDE.png" style="width: 600px;" />

<br>

### HealthDisplay

Shows mobs' name and health in their nameplates (the name text above their head)

<img src="img/249BD038-C12F-4640-BA52-99ECB8B8ABEA.jpeg" style="width: 600px;" />

<br>

### Panorama Generator

Generate a set of 6 square images in the format of the title screen panorama. Incompatible with shaders, unfortunately.

<br>

### Zoom

Look at far-away objects! Adjustable with the scroll wheel.

Compatible with Australian Mode!

<br>

## Misc

### **BlackBox

Records information like your position, health and death point(s) as you play. A Flight Data Recorder for Minecraft!

Work in progress.

<br>

### CommandProcessor

Enable/disable features, change their configs, and run macros, all from the chat.

(I'm gonna move all of this to Brigadier after exams, just bear with it for a little longer :/)

**Limitations**

- No command history access with arrow keys
- Many features don't support config changes through commands
- No autocomplete / hinting

<br>

### ConfigMenu

CompleteConfig configuration menu.

(yes)

<br>

### FeatureList

Shows your Xenon version and the features you have enabled.

<br>

### LazyDFU Integration

Optimises DFU (DataFixerUpper) just like LazyDFU!

<br>

**Coming soon**
