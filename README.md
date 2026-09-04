# CreateTrainWebAPI

This mod adds HTTP endpoints for Create train position and railway-network data. It currently targets NeoForge 1.21.1. To install it, place the jar file in the server's `mods` folder.

By default, the web API listens on port `8080`. The host, port, and train-model directory can be changed in the config file.

## BlueMap integration

When used together with [BluemapCreateEntityAddon](https://github.com/BeneHenke/BluemapCreateEntityAddon), Create train models can be generated for the web overlay.

This fork includes a BlueMap 5.7-compatible integration:

- `bluemap/train.js` — railway and live 3D train overlay
- `bluemap/train-settings.js` — optional drawing-settings GUI
- `bluemap/train-labels.js` — optional live train-name markers
- `bluemap/create-train-bootstrap.js` — recommended deterministic loader for BlueMap 5.7

The built-in **Markers** menu gains:

- `Create 路線図` — railway lines, stations, and inter-dimensional portals
- `Create 列車` — live Create train positions
- `Create 列車名` — live train-name markers with current coordinates

The overlay reads the railway network from `/network`, receives live train updates from `/trainsLive`, and loads generated train models from `/trainModels/`.

### Important: BlueMap 5.7 script ordering

BlueMap 5.7 stores the `scripts` entries from `webapp.conf` in a Java `HashSet`. Therefore the order written in `webapp.conf` is not guaranteed to be preserved.

For that reason, the recommended setup is to load only `create-train-bootstrap.js` from BlueMap. The bootstrap then loads the remaining scripts sequentially in this order:

1. `create-train-config.js`
2. `train.js`
3. `train-settings.js`
4. `train-labels.js`

`create-train-config.js`, `train-settings.js`, and `train-labels.js` are optional to the bootstrap. `train.js` is required.

### Configure the API URL

The BlueMap scripts use `http://localhost:8080` by default. For a remotely hosted BlueMap, create `create-train-config.js` in the same web directory as the integration scripts:

```js
window.CREATE_TRAIN_WEB_API_URL = "https://train-api.example.com";
window.CREATE_TRAIN_LINES_THROUGH_TERRAIN = true;
window.CREATE_TRAIN_TRAINS_THROUGH_TERRAIN = false;
window.CREATE_TRAIN_LABEL_MAX_DISTANCE = 4096;
```

Then configure BlueMap 5.7 to load only the bootstrap:

```hocon
scripts: [
    "create-train-bootstrap.js"
]
```

This avoids relying on BlueMap's unordered custom-script collection.

### Live train-name markers

`bluemap/train-labels.js` adds `Create 列車名` as an independent MarkerSet. Each visible Create train is represented by a BlueMap `HtmlMarker` that follows the leading carriage position.

Opening `Create 列車名` in BlueMap's **Markers** menu shows each train name and its current `(X | Y | Z)` coordinates. Clicking a train entry moves the map to that train. Create component strings such as `literal{Express}` are displayed as `Express`.

Train-name labels use BlueMap's normal distance fading. By default they are fully hidden at **4096 blocks**, which keeps the map readable when zoomed far out. Override the limit with `window.CREATE_TRAIN_LABEL_MAX_DISTANCE` in `create-train-config.js`.

### Drawing-settings GUI

`bluemap/train-settings.js` adds a `Create 描画設定` button to BlueMap's main side menu. Visibility remains controlled by the MarkerSets; this GUI only controls depth rendering:

- `路線図を地形越しに表示`
- `列車を地形越しに表示`

Defaults:

- railway overlay through terrain: **enabled**
- trains through terrain: **disabled**

Settings are stored in browser `localStorage`.

### Install the overlay

1. Copy `bluemap/create-train-bootstrap.js`, `bluemap/train.js`, and any optional integration scripts you want to the BlueMap web root.
2. For a remote API, create `create-train-config.js` in the same directory.
3. Set `webapp.conf` to load only `create-train-bootstrap.js`.
4. Reload BlueMap and hard-refresh the browser.
5. Open BlueMap's **Markers** menu to control `Create 路線図`, `Create 列車`, and `Create 列車名`.
6. If `train-settings.js` is present, use `Create 描画設定` in the main side menu.

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
