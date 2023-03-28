## CommandProcessor Documentation

Documentation for Xenon's command processor

### Basic command structure

`[prefix character][feature / command name][action (for feature), or argument][more arguments...]`

Example feature command: `!fullkeyboard enable`
"!" - prefix
"fullkeyboard" - feature name
"enable" - action

Example standalone command: `!help listf`
"!" - prefix
"help" - command name
"listf" - argument

### Feature action list

- `e, enable, on`: Enable the target feature
- `d, disable, off`: Disable the target feature
- `set`: Change a setting of the target feature (Note: implementation across features is incomplete)
- `exec`: Execute an action on the target feature (not used)

### Feature name list

`TODO`

### Standalone command list

`TODO`
