
This mod adds an endpoint for the postion data of the create trains. At the moment it is compatible with Neoforge 1.21.1. To install simply place the jar file into the mods folder. By default it uses port 8080, this can be changed in the config file. 

When used together with https://github.com/BeneHenke/BluemapCreateEntityAddon the path to the train models can be configured in the config aswell. To display the trains in Bluemap add https://github.com/BeneHenke/BluemapCreateEntityAddon/blob/BlueMapAddon/bluemap/train.js to the scripts and change the url to your endpoint.

Example config:
```
#Webserver Port
# Default: 8080
# Range: 1 ~ 65535
serverPort = 8080
#Webserver hostname
serverHost = "0.0.0.0"
#Path of the train models
trainModelPath = "bluemap/train_models/"
```
