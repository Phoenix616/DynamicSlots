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
# Minimum amount of slots that should be displayed
min-slots: 0
# Maximum amount of slots that should be displayed
# Will do nothing when set over the actual slot count that the server has
max-slots: 10000
# Amount of slots to add when the online count is the same or above the count that would be displayed
# Use 0 to disable this feature completely
# Negative values will substract from the real playercount
# (Why you would ever do that is beyond me but you can if you want!)
add-when-full: 1
# The message to use when a player tries to connect to a full server
# You can use the variables %players% and %slots% in the query to insert the online count and slot count
full-kick-message: 'Server is full!'
# The source where we want to get the slots from
source:
  # Possible types include mysql, file, static and url
  type: static
  # The mysql query, file path or web url
  # You can use the variables %players% and %slots% in the query to insert the online count and slot count
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
    DynamicSlots Minecraft plugin
    Copyright (C) 2022 Max Lee aka Phoenix616 (max@themoep.de)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
