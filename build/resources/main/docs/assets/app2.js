//const params = new URLSearchParams(window.location.search);
//const host = params.get("url");

//if (!host) {
//  alert("Kein URL-Parameter angegeben, z. B. ?url=192.168.1.108");
//  throw new Error("Kein URL-Parameter angegeben");
//}

//document.getElementById("domain").innerHTML = host;
const wsUrl = `ws://${location.hostname}:8082`;

const tbody = document.querySelector("#player_stats tbody");
const statusSpan = document.getElementById("ws_connect");

const players = {};
const playerRows = {};
let ws;

connectWebSocket();

function connectWebSocket() {
    setStatus("Verbindung wird hergestellt...", "warning");

    ws = new WebSocket(wsUrl);

    ws.onopen = () => {
        console.log("Verbunden");
        setStatus("Verbunden", "success");
        ws.send(JSON.stringify({ action: "send_first_data" }));
    };

    ws.onmessage = (event) => {
        console.log("Empfangen:", event.data);

        try {
            const data = JSON.parse(event.data);
            const name = Object.keys(data)[0];
            const payload = data[name];

            // ===== SERVER NACHRICHT =====
            if (name === "server") {
                const span = document.getElementById(payload.type);
                if (span) {
                    span.textContent = payload.value;
                }
                return;
            }

            // ===== SPIELER DATEN =====
            if (!players[name]) {
                players[name] = {
                    name: name,
                    deaths: "",
                    health: "",
                    level: "",

                    playTime: "",
                    lastLogin: "",
                    online: false,
                    mined_dirt: ""
                };

                createRow(name);
            }

            let value = payload.value;

            if (payload.type === "online") {
                value = value === "true";
            }

            if (
                payload.type === "deaths" ||
                payload.type === "health" ||
                payload.type === "level" ||
                payload.type === "playTime" ||
                payload.type === "lastLogin"
            ) {
                value = Number(value);
            }

            players[name][payload.type] = value;
            updateRow(name);

        } catch (err) {
            console.error("JSON-Fehler:", err);
        }
    };

    ws.onerror = (err) => {
        console.error("WebSocket-Fehler:", err);
        setStatus("Offline", "error");
    };

    ws.onclose = () => {
        console.warn("Verbindung getrennt");
        setStatus("Offline", "error");

        setTimeout(() => {
            connectWebSocket();
        }, 10000);
    };
}

function setStatus(text, className) {
    statusSpan.textContent = text;
    statusSpan.classList.remove("success", "error", "warning");
    statusSpan.classList.add(className);
}

function createRow(playerName) {
    const tr = document.createElement("tr");

    tr.innerHTML = `
        <td class="col-id"></td>
        <td class="col-name"></td>
        <td class="col-health"></td>
        <td class="col-level"></td>
        <td class="col-deaths"></td>
        <td class="col-playtime"></td>
        <td class="col-lastlogin"></td>
        <td class="col-online"></td>

    `;

    tbody.appendChild(tr);
    playerRows[playerName] = tr;

    updateRowNumbers();
}

function updateRow(playerName) {
    const player = players[playerName];
    const row = playerRows[playerName];

    if (!row) return;

    row.querySelector(".col-name").textContent = player.name || "-";
    row.querySelector(".col-health").textContent =
        player.health === "" || player.health == null ? "-" : player.health;
    row.querySelector(".col-level").textContent =
        player.level === "" || player.level == null ? "-" : player.level;
    row.querySelector(".col-deaths").textContent =
        player.deaths === "" || player.deaths == null ? "-" : player.deaths;
    row.querySelector(".col-playtime").textContent = formatPlayTime(player.playTime);
    row.querySelector(".col-lastlogin").textContent = formatLastLogin(player.lastLogin);
    row.querySelector(".col-online").textContent = formatOnline(player.online);
}

function updateRowNumbers() {
    const rows = tbody.querySelectorAll("tr");
    rows.forEach((row, index) => {
        row.querySelector(".col-id").textContent = index + 1;
    });
}

function formatPlayTime(minutes) {
    if (minutes === "" || minutes == null || isNaN(minutes)) return "-";

    const h = Math.floor(minutes / 60);
    const m = minutes % 60;

    return `${h}h ${m}m`;
}

function formatLastLogin(timestamp) {
    if (!timestamp || isNaN(timestamp)) return "-";

    const diffMs = Date.now() - Number(timestamp);
    const diffMin = Math.floor(diffMs / 1000 / 60);
    const diffStd = Math.floor(diffMin / 60);
    const diffTage = Math.floor(diffStd / 24);

    if (diffMin < 1) return "vor wenigen Sekunden";
    if (diffMin < 60) return `vor ${diffMin} Min.`;
    if (diffStd < 24) return `vor ${diffStd} Std.`;
    return `vor ${diffTage} Tagen`;
}

function formatOnline(online) {
    if (online === true) return "Ja";
    if (online === false) return "Nein";
    return "-";
}