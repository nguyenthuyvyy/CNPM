import axios from 'axios';
const base = axios.create({ baseURL: 'http://localhost:8085' });

export const getAllDrones = () => base.get('/api/drones').then(r => r.data);
export const createDrone = (drone) => base.post('/api/drones', drone).then(r => r.data);
export const updateDrone = (id, drone) => base.put(`/api/drones/${id}`, drone).then(r => r.data);
export const deleteDrone = (id) => base.delete(`/api/drones/${id}`);
