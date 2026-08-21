# CreateTrainWebAPI

This mod adds HTTP endpoints for Create train position and railway-network data. It currently targets NeoForge 1.21.1. To install it, place the jar file in the server's `mods` folder.

By default, the web API listens on port `8080`. The host, port, and train-model directory can be changed in the config file.

## BlueMap integration

When used together with [BluemapCreateEntityAddon](https://github.com/BeneHenke/BluemapCreateEntityAddon), Create train models can be generated for the web overlay.

This fork also includes [`bluemap/train.js`](bluemap/train.js), a BlueMap 5.7-compatible train overlay. It adds two toggleable MarkerSets to BlueMap's built-in **Markers** menu:

- `Create 路線図` — railway lines, stations, and inter-dimensional portals
- `Create 列車` — live Create train positions

The overlay reads the railway network from `/network`, receives live train updates from `/trainsLive`, and loads generated train models from `/trainModels/`.

### Configure the API URL

`bluemap/train.js` uses `http://localhost:8080` by default. For a remotely hosted BlueMap, define `window.CREATE_TRAIN_WEB_API_URL` before loading `train.js`.

For example, create `create-train-config.js` in the BlueMap web root:

```js
window.CREATE_TRAIN_WEB_API_URL = "https://train-api.example.com";
```

Then load the config script before `train.js` in BlueMap's `webapp.conf`:

```hocon
scripts: [
    "create-train-config.js",
    "train.js"
]
```

The overlay removes any trailing `/` from the configured API URL automatically.

### Install the overlay

1. Copy `bluemap/train.js` to the BlueMap web root.
2. Add `train.js` to the `scripts` list in `webapp.conf`.
3. If the API is not available at `http://localhost:8080`, add a config script as shown above and load it before `train.js`.
4. Reload BlueMap and hard-refresh the browser.
5. Open BlueMap's **Markers** menu. `Create 路線図` and `Create 列車` should be available as independent visibility toggles.

The BlueMap page must be able to reach the configured API URL. If BlueMap and the API use different origins, the API must return appropriate CORS headers. HTTPS BlueMap deployments should also expose the API over HTTPS to avoid mixed-content blocking.

## Example server config

```hocon
# Webserver Port
# Default: 8080
# Range: 1 ~ 65535
serverPort = 8080

# Webserver hostname
serverHost = "0.0.0.0"

# Path of the train models
trainModelPath = "bluemap/train_models/"
```
