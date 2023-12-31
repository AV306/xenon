## CommandProcessor Documentation

Documentation for Xenon's command processor!

~~(I could have just used client-side commands with Bridgadier, but it's too late to change now. Ah well)~~

~~(Ok you know what, as soon as finals finish I'm moving everything to Brigadier; just bear with my terrible spaghetti code for a while longer)~~

(I did it! Slash commands are now supported! The basic command structure is the same, except with a slash as the prefix)

### Basic command structure

`<prefix character><feature / command name> [action (for feature) / argument (for command] [more arguments...]`

Example feature command: `!fullkeyboard enable`
"!" - prefix
"fullkeyboard" - feature name
"enable" - action

Example standalone command: `!help listf` (Note: this command is broken)
"!" - prefix
"help" - command name
"listf" - argument

### Feature action list

- No action (Slash command only): Toggle the feature if possible, otherwise runs it
- `e, enable, on`: Enable the target feature
- `d, disable, off`: Disable the target feature
- `set`: Change a setting of the target feature (Note: implementation across features is incomplete)
- `exec`: Execute an action on the target feature (not used)

### Feature name list

See [the feature documentation](/FEATURES.md).

### Standalone command list

- `crash`: Crashes Minecraft with an out-of-bounds array access

`TODO`
