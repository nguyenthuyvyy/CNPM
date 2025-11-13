import React, { useState } from "react";
import DroneMap from "../components/DroneMap"; // bản map an toàn
import { getAllDrones, createDrone } from "../api/drone";

export default function OwnerDashboard() {
  const [drones, setDrones] = useState([]);

  const addSampleDrone = () => {
    const newDrone = {
      id: Math.floor(Math.random() * 1000),
      name: "Drone " + Math.floor(Math.random() * 100),
      model: "X1",
      batteryLevel: 100,
      status: "AVAILABLE",
      latitude: 10.762622,
      longitude: 106.660172,
    };
    setDrones(prev => [...prev, newDrone]);
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <h1 className="text-3xl font-bold mb-4">📡 Owner Dashboard</h1>
      <button
        onClick={addSampleDrone}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        ➕ Add Sample Drone
      </button>

      <div className="mt-6">
        <DroneMap initialDrones={drones} safeMode />
      </div>

      <div className="mt-8 bg-white shadow rounded p-4">
        <h2 className="text-xl font-semibold mb-4">📋 Drone List</h2>
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-gray-200 text-left">
              <th className="border px-3 py-2">ID</th>
              <th className="border px-3 py-2">Name</th>
              <th className="border px-3 py-2">Model</th>
              <th className="border px-3 py-2">Status</th>
              <th className="border px-3 py-2">Battery</th>
            </tr>
          </thead>
          <tbody>
            {drones.map(d => (
              <tr key={d.id}>
                <td className="border px-3 py-2">{d.id}</td>
                <td className="border px-3 py-2">{d.name}</td>
                <td className="border px-3 py-2">{d.model}</td>
                <td className="border px-3 py-2">{d.status}</td>
                <td className="border px-3 py-2">{d.batteryLevel}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
