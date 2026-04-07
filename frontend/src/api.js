import axios from 'axios';

const client = axios.create({
  baseURL: '',
  timeout: 10000
});

export const api = {
  tags() {
    return client.post('/api/tags/list', {});
  },
  createTag(payload) {
    return client.post('/api/tags/create', payload);
  },
  updateTag(payload) {
    return client.post('/api/tags/update', payload);
  },
  deleteTag(id) {
    return client.post('/api/tags/delete', { id });
  },
  cards(payload) {
    return client.post('/api/cards/list', payload || {});
  },
  createCard(payload) {
    return client.post('/api/cards/create', payload);
  },
  updateCard(payload) {
    return client.post('/api/cards/update', payload);
  },
  deleteCard(id) {
    return client.post('/api/cards/delete', { id });
  },
  scenes() {
    return client.post('/api/scenes/list', {});
  },
  scene(id) {
    return client.post('/api/scenes/get', { id });
  },
  sceneCards(id) {
    return client.post('/api/scenes/cards', { id });
  },
  createScene(payload) {
    return client.post('/api/scenes/create', payload);
  },
  updateScene(payload) {
    return client.post('/api/scenes/update', payload);
  },
  deleteScene(id) {
    return client.post('/api/scenes/delete', { id });
  },
  setCheck(sceneId, cardId, checked) {
    return client.post('/api/scenes/check', { sceneId, cardId, checked });
  },
  resetChecks(sceneId) {
    return client.post('/api/scenes/reset', { id: sceneId });
  }
};
