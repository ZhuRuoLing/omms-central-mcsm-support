# OMMS Central MCSM Support
Add your MCSManager instances as a OMMS Central controller!  
This plugin bypass MCSManager web and directly connect to MCSManager Daemon.

## Config File

```json lines
[
  { // some daemon
    "accessToken": "114514", // access token to daemon
    "address": "127.0.0.1:24444", // daemon ip address and its port
    "name": "mcsm_daemon_1" // a custom name
  },
  { // another daemon
    "accessToken": "1919810", // access token to daemon
    "address": "127.0.0.1:24445", // daemon ip address and its port
    "name": "mcsm_daemon_2" // a custom name
  }
  // other daemons...
]
```

## Links
[MCSManager](https://github.com/MCSManager)  
[Oh My Minecraft Server](https://github.com/OhMyMinecraftServer)  
[OMMS Central](https://github.com/OhMyMinecraftServer/omms-central)  