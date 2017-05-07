# DynamicSlots
Dynamically set the slots of your BungeeCord or Bukkit Minecraft server! You can define multiple dynamic sources from where the plugin will get your slot/max player count from! (Or a static one)

## Commands

Aliases: `/dynamicslots`, `dynamicslot`, `/ds`

Permission: `dynamicslots.command`

| Usage             | Permission                    | Help                                                           |
|-------------------|-------------------------------|----------------------------------------------------------------|
| `/ds info`        | `dynamicslots.command.info`   | Shows some information about your current setup                |
| `/ds reload`      | `dynamicslots.command.reload` | Reload the config                                              |
| `/ds set <count>` | `dynamicslots.command.set`    | Manually set the slot count an overwrite any configured amount |
| `/ds reset`       | `dynamicslots.command.reset`  | Reset a manually set count back to the configured one          |

## Config

```yaml
# How long to cache the results of the source's query for
cache-duration: 300
# Slot count used when static source is used or no dynamic slot count was found
# Setting slots to -1 will cause it to display the real slot count in that case
static-slots: 100
# The source where we want to get the slots from
source:
  # Possible types include mysql, file, static and url
  type: static
  # The mysql query, file path or web url
  query: 'select COUNT(*) as count from `lb-players` WHERE lastlogin > DATE_ADD(NOW(), INTERVAL -1 DAY) AND onlinetime > 60'
  # The regex pattern to try to get the count from when the query returns a string
  regex: ''
  # Additional settings for the mysql source
  username: username
  password: password1
  database: mydatabase
  host: localhost
  port: 3306
  ```
  
  ## Downloads
  
  Development Builds: https://ci.minebench.de/job/DynamicSlots/
  
  ## License
  
  ```
Copyright 2017 Max Lee (https://github.com/Phoenix616/)
This program is free software: you can redistribute it and/or modify
it under the terms of the Mozilla Public License as published by
the Mozilla Foundation, version 2.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
Mozilla Public License v2.0 for more details.
You should have received a copy of the Mozilla Public License v2.0
along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
```
