<script>
  import { onMount } from "svelte";
  import L from "leaflet";
  import "leaflet/dist/leaflet.css";

  export let address = "";
  let map;

  $: {
    if (address) {
      updateMap(address);
    }
  }

  async function updateMap(address) {
    const coordinates = await getCoordinates(address);

    if (coordinates) {
      if (!map) {
        map = L.map("map").setView(coordinates, 15);

        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);
      } else {
        map.setView(coordinates, 15);
      }

      let customIcon = L.icon({
        iconUrl: "../marker.png",
        iconSize: [50, 60],
        iconAnchor: [22, 94],
        popupAnchor: [-3, -76],
      });

      L.marker(coordinates, { icon: customIcon }).addTo(map);
    } else {
      console.error("Could not find coordinates for the address");
    }
  }

  async function getCoordinates(address) {
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
        address
      )}`
    );

    if (response.ok) {
      const results = await response.json();
      if (results.length > 0) {
        const { lat, lon } = results[0];
        return [parseFloat(lat), parseFloat(lon)];
      }
    }
    return null;
  }
</script>

<style>
  #map {
    width: 100%;
    height: 300px;
  }
</style>

<div id="map"></div>
