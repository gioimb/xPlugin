# Minecraft xPlugin

- WebServer, WebSocket Stats Server, Stats Plugin, 
- Custom Tablist Plugin
- Custom Join/Leave and Welcome Message
- Silkspsawner PLugin
- Bank System

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


- This Plugin allows players to mine spawners with Silk Touch.
- You can configure which tools are allowed and whether everyone or only players with permission can mine spawners. (Permission: `xplugin.break.spawner`)


- One Player Sleep: that allows one player to skip the night by sleeping


- Bank System

### Issues
- Webinterface Motd is not displayed correctly
- Player Health update not correct

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
| `money`      | int    | Get the amount of Money      |

example: 
```
{"server": {"type":"server_version", "value":"1.21.11"}}
{"ericimbriaco":{"type":"lastLogin","value":1774881995944}}
```


## Added Commands
| Command          | Permission                                             | Description                                             |
|------------------|--------------------------------------------------------|---------------------------------------------------------|
| /xplugin         | `xplugin.command.xplugin`                              | Get Plugin Version                                      |
| /heal <Player>   | `xplugin.command.heal xplugin.command.heal.others`     | Heal a Player                                           |
| /crash [Player]  | `xplugin.command.crash.others`                         | Kicks the player with a Java error message              |
| /repair [Player] | `xplugin.command.repair xplugin.command.repair.others` | Repair the tool the player is holding.                  |
| /top [Player]    | `xplugin.command.top xplugin.command.top.others`       | Teleport to the highest block at your current position. |

| Command                      | Permission                                                        | Description                                    |
|------------------------------|-------------------------------------------------------------------|------------------------------------------------|
| /bank pay <Player> <Amount>  | `xplugin.command.bank.pay`                                        | Send money to another player                   |
| /bank balance <Player>       | `xplugin.command.bank.balance xplugin.command.bank.balance.other` | Check your balance or another player's balance |
| /bank help                   | `xplugin.command.bank.help`                                       | Show Commands                                  | 
| /bank give <Player> <Amount> | `xplugin.command.bank.give`                                       | Admin: Set a player's bank balance             |
| /bank set <Player <Amount    | `xplugin.command.bank.set`                                        | Admin: Add money to a player's bank account    | 