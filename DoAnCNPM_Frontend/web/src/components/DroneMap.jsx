import React from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// ✅ Import icons as ES modules (Vite-friendly)
import iconRetinaUrl from "leaflet/dist/images/marker-icon-2x.png";
import iconUrl from "leaflet/dist/images/marker-icon.png";
import shadowUrl from "leaflet/dist/images/marker-shadow.png";

// Fix icon for leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
});

export default function DroneMap({ initialDrones = [] }) {
  return (
    <MapContainer
      center={[10.762622, 106.660172]}
      zoom={13}
      style={{ height: "500px", width: "100%" }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      {initialDrones.map((d) => (
        <Marker
          key={d.id}
          position={[d.latitude || 10.762622, d.longitude || 106.660172]}
        >
          <Popup>
            <b>{d.name}</b>
            <br />
            Model: {d.model}
            <br />
            Battery: {d.batteryLevel}%
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
