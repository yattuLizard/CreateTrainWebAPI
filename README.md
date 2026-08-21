# CreateTrainWebAPI

This mod adds HTTP endpoints for Create train position and railway-network data. It currently targets NeoForge 1.21.1. To install it, place the jar file in the server's `mods` folder.

By default, the web API listens on port `8080`. The host, port, and train-model directory can be changed in the config file.

## BlueMap integration

When used together with [BluemapCreateEntityAddon](https://github.com/BeneHenke/BluemapCreateEntityAddon), Create train models can be generated for the web overlay.

This fork also includes [`bluemap/train.js`](bluemap/train.js), a BlueMap 5.7-compatible train overlay. It adds two toggleable MarkerSets to BlueMap's built-in **Markers** menu:

- `Create 路線図` — railway lines, stations, and inter-dimensional portals
- `Create 列車` — live Create train positions

The overlay reads the railway network from `/network`, receives live train updates from `/trainsLive`, and loads generated train models from `/trainModels/`.

### Optional live train-name markers

[`bluemap/train-labels.js`](bluemap/train-labels.js) adds a third top-level MarkerSet:

- `Create 列車名` — live labels for the trains currently visible in the selected BlueMap world

Each train is represented by a BlueMap `HtmlMarker` that follows the leading carriage position. Opening `Create 列車名` in BlueMap's **Markers** menu shows the individual train names and their current `(X | Y | Z)` coordinates. Clicking an individual train entry moves the map to that train.

The complete train-name MarkerSet can be enabled or disabled independently from `Create 列車`.

Create component strings such as `literal{Express}` are displayed as `Express`.

### Optional drawing-settings GUI

[`bluemap/train-settings.js`](bluemap/train-settings.js) adds a `Create 描画設定` button to BlueMap's main side menu. Visibility remains controlled by the MarkerSets above; this GUI only controls depth rendering:

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

The BlueMap scripts use `http://localhost:8080` by default. For a remotely hosted BlueMap, define `window.CREATE_TRAIN_WEB_API_URL` before loading them.

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
    "train-settings.js",
    "train-labels.js"
]
```

`train-settings.js` and `train-labels.js` are optional. `train-labels.js` uses the same `/network` and `/trainsLive` API and maintains its own lightweight live-data connection so it can remain independent from the 3D train renderer.

The scripts remove any trailing `/` from the configured API URL automatically.

### Install the overlay

1. Copy `bluemap/train.js` to the BlueMap web root.
2. Optionally copy `bluemap/train-settings.js` to enable the drawing-settings GUI.
3. Optionally copy `bluemap/train-labels.js` to enable live train-name markers.
4. Add the scripts you copied to the `scripts` list in `webapp.conf`.
5. If the API is not available at `http://localhost:8080`, add a config script as shown above and load it before the other Create train scripts.
6. Reload BlueMap and hard-refresh the browser.
7. Open BlueMap's **Markers** menu. `Create 路線図`, `Create 列車`, and, when enabled, `Create 列車名` are independent visibility toggles.
8. Open `Create 列車名` to see individual train names and current coordinates; click a train to move the map to it.
9. If `train-settings.js` is loaded, open the main side menu and use `Create 描画設定` to change through-terrain rendering.

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
