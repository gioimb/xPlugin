# Minecraft xPlugin

## v1.0

### Changelog
- Add config.yml
- Opens a web interface on port 8080 (Port can be changed in the config.yml) for player stats
- Opens a WebSocket server on port 8082 (Port can be changed in the config.yml)
  - Sends Player and Server Stats (can be enabled or disabled in config.yml)
  - If you want to receive all available data at once, you need to send a JSON message to the server once.  `{"action":"send_first_data"}`
  - Otherwise, the server sends the data dynamically when it changes.

- Custom player join and leave messages (The message can be changed in the config.yml)
- Custom Join Message (can be changed in the config.yml)
- Custom Text in Tablist (Header/Footer can be changed in the config.yml)

### Issues
- Webinterface Motd is not displayed correctly

### WebSocket Stats
Server Data

| JSON Type        | Value  | Description                |
|------------------|--------|----------------------------|
| `server_ip`      | String | Servername from config.yml |
| `server_version` | String | Server Version             |
| `maxSlots`       | int    | get max Server Slots       |
| `usedSlots`      | int    | get online Player          |
| `motd`           | String | get server motd            |

Player Data

| JSON Type    | Value  | Description                  |
|--------------|--------|------------------------------|
| `name`       | String | Get players name             |
| `deaths`     | int    | Get player worlds death      |
| `level`      | int    | Get players xp               |
| `deaths`     | int    | Get players deaths           |
| `playTime`   | int    | Get players play pime        |
| `lastlogin`  | int    | Get the last log in          |
| `online`     | bool   | Get Players Online State     |
| `mined_dirt` | int    | Get the amount of Mined Dirt |

example: 
```
{"server": {"type":"server_version", "value":"1.21.11"}}
{"ericimbriaco":{"type":"lastLogin","value":1774881995944}}
```


## Added Commands
| Command         | Permission                                         | Description                                |
|-----------------|----------------------------------------------------|--------------------------------------------|
| /xplugin        | `xplugin.command.xplugin`                          | Get Plugin Version                         |
| /heal <Player>  | `xplugin.command.heal xplugin.command.heal.others` | Heal a Player                              |
| /crash [Player] | `xplugin.command.crash.others`                     | Kicks the player with a Java error message |