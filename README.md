# CreateTrainWebAPI

This mod adds HTTP endpoints for Create train position and railway-network data. It currently targets NeoForge 1.21.1. To install it, place the jar file in the server's `mods` folder.

By default, the web API listens on port `8080`. The host, port, and train-model directory can be changed in the config file.

## BlueMap integration

When used together with [BluemapCreateEntityAddon](https://github.com/BeneHenke/BluemapCreateEntityAddon), Create train models can be generated for the web overlay.

This fork also includes [`bluemap/train.js`](bluemap/train.js), a BlueMap 5.7-compatible train overlay. It adds two toggleable MarkerSets to BlueMap's built-in **Markers** menu:

- `Create 路線図` — railway lines, stations, and inter-dimensional portals
- `Create 列車` — live Create train positions

The overlay reads the railway network from `/network`, receives live train updates from `/trainsLive`, and loads generated train models from `/trainModels/`.

### Optional drawing-settings GUI

[`bluemap/train-settings.js`](bluemap/train-settings.js) adds a `Create 描画設定` button to BlueMap's main side menu. Visibility remains controlled by the two MarkerSets above; this GUI only controls depth rendering:

- `路線図を地形越しに表示` — show the railway overlay through terrain
- `列車を地形越しに表示` — show trains through terrain

The defaults are:

- railway overlay through terrain: **enabled**
- trains through terrain: **disabled**

Settings are stored in the browser's `localStorage`, so changes persist across reloads. The GUI is language-independent and does not search for BlueMap menu labels such as `Info`.

Optional default values can be set before the scripts load:

```js
window.CREATE_TRAIN_LINES_THROUGH_TERRAIN = true;
window.CREATE_TRAIN_TRAINS_THROUGH_TERRAIN = false;
```

### Configure the API URL

`bluemap/train.js` uses `http://localhost:8080` by default. For a remotely hosted BlueMap, define `window.CREATE_TRAIN_WEB_API_URL` before loading `train.js`.

For example, create `create-train-config.js` in the BlueMap web root:

```js
window.CREATE_TRAIN_WEB_API_URL = "https://train-api.example.com";
window.CREATE_TRAIN_LINES_THROUGH_TERRAIN = true;
window.CREATE_TRAIN_TRAINS_THROUGH_TERRAIN = false;
```

Then load the scripts in BlueMap's `webapp.conf`:

```hocon
scripts: [
    "create-train-config.js",
    "train.js",
    "train-settings.js"
]
```

`train-settings.js` waits for `train.js` to initialize before installing its render-loop override, so it is tolerant of BlueMap's dynamic script loading.

The overlay removes any trailing `/` from the configured API URL automatically.

### Install the overlay

1. Copy `bluemap/train.js` to the BlueMap web root.
2. Optionally copy `bluemap/train-settings.js` to the BlueMap web root to enable the drawing-settings GUI.
3. Add `train.js` and, if used, `train-settings.js` to the `scripts` list in `webapp.conf`.
4. If the API is not available at `http://localhost:8080`, add a config script as shown above.
5. Reload BlueMap and hard-refresh the browser.
6. Open BlueMap's **Markers** menu. `Create 路線図` and `Create 列車` should be available as independent visibility toggles.
7. If `train-settings.js` is loaded, open the main side menu and use `Create 描画設定` to change through-terrain rendering.

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
