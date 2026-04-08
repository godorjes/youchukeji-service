<template>
  <div class="page">
    <div class="phone">
      <div class="status-bar">
        <span>{{ currentTime }}</span>
        <div class="status-icons">
          <div class="battery"><div class="battery-fill" :style="{ width: batteryLevel + '%' }"></div></div>
        </div>
      </div>

      <div class="content">
        <div v-if="currentView === 'home'" class="view">
          <header class="header">
            <div>
              <h1>场景</h1>
              <p>用标签组合场景，告别层级结构</p>
            </div>
            <button class="icon-btn" @click="openTagView">
              <span class="icon">🏷️</span>
            </button>
          </header>

          <div class="search-wrap">
            <span class="search-icon">🔍</span>
            <input v-model="sceneSearch" class="search-input" placeholder="搜索场景..." />
            <button v-if="sceneSearch" class="search-clear" @click="sceneSearch = ''">✕</button>
          </div>

          <template v-if="searchResults !== null">
            <section class="section">
              <div class="section-title">
                <span>搜索结果 · {{ searchResults.records.length }} 个</span>
              </div>
              <div v-if="searchResults.records.length" class="list">
                <button v-for="scene in searchResults.records" :key="scene.id" class="list-item" @click="openScene(scene)">
                  <div class="emoji small">{{ scene.icon }}</div>
                  <div class="list-main">
                    <div class="title">{{ scene.name }}</div>
                    <div class="sub">{{ tagNames(scene.tags) }}</div>
                  </div>
                  <div class="count">{{ scene.checkedCount }}/{{ scene.totalCount }}</div>
                  <span class="chevron">›</span>
                </button>
              </div>
              <div v-else class="empty-tip">未找到匹配的场景</div>
            </section>
          </template>

          <template v-else>
            <section v-if="scenePinned.length" class="section">
              <div class="section-title">
                <span class="dot yellow"></span>
                <span>置顶场景</span>
              </div>
              <div class="grid">
                <button v-for="scene in scenePinned" :key="scene.id" class="card" @click="openScene(scene)">
                  <div class="progress" :style="{ width: percent(scene) + '%' }"></div>
                  <div class="emoji">{{ scene.icon }}</div>
                  <div class="title">{{ scene.name }}</div>
                  <div class="sub">{{ scene.checkedCount }}/{{ scene.totalCount }} 已确认</div>
                  <button class="pin pin-btn" @click.stop="togglePin(scene)">📌</button>
                </button>
              </div>
            </section>

            <section v-if="sceneOthers.records.length || sceneOthers.total > 0" class="section">
              <div class="section-title">
                <span class="dot gray"></span>
                <span>其他场景</span>
              </div>
              <div class="list">
                <button v-for="scene in sceneOthers.records" :key="scene.id" class="list-item" @click="openScene(scene)">
                  <div class="emoji small">{{ scene.icon }}</div>
                  <div class="list-main">
                    <div class="title">{{ scene.name }}</div>
                    <div class="sub">{{ tagNames(scene.tags) }}</div>
                  </div>
                  <div class="count">{{ scene.checkedCount }}/{{ scene.totalCount }}</div>
                  <span class="chevron">›</span>
                  <button class="pin-btn pin-inline" @click.stop="togglePin(scene)">📌</button>
                </button>
              </div>
              <button v-if="othersRemaining > 0" class="expand-btn" :disabled="sceneOthersLoading" @click="loadMoreScenes">
                {{ sceneOthersLoading ? '加载中...' : `加载更多 · 还剩 ${othersRemaining} 个` }}
                <span v-if="!sceneOthersLoading" class="expand-arrow">↓</span>
              </button>
              <button v-if="sceneOthers.page > 1" class="expand-btn" @click="collapseScenes">
                收起
                <span class="expand-arrow">↑</span>
              </button>
            </section>
          </template>

          <section v-if="searchResults === null" class="section">
            <div class="section-title">
              <span class="dot gray"></span>
              <span>最近卡片</span>
            </div>
            <div class="pill-list">
              <div v-for="card in recentCards.slice(0, 6)" :key="card.id" class="pill">
                <span class="pill-text">{{ card.title }}</span>
                <div class="pill-dots">
                  <span v-for="tag in card.tags.slice(0, 2)" :key="tag.id" class="dot" :class="tag.color"></span>
                </div>
              </div>
              <div v-if="recentCards.length > 6" class="pill pill-more">
                +{{ recentCards.length - 6 }}
              </div>
            </div>
          </section>
        </div>

        <div v-if="currentView === 'scene'" class="view">
          <header class="header scene-header">
            <button class="icon-btn" @click="backHome">‹</button>
            <button class="icon-btn" @click="resetChecks">⟲</button>
          </header>
          <div class="scene-info">
            <div class="emoji big">{{ selectedScene.icon }}</div>
            <div>
              <h2>{{ selectedScene.name }}</h2>
              <div class="tag-row">
                <span v-for="tag in selectedScene.tags" :key="tag.id" class="tag" :class="tag.color">{{ tag.name }}</span>
              </div>
            </div>
          </div>
          <div class="progress-row">
            <span>{{ checkedCount }}/{{ sceneCards.length }} 已确认</span>
            <span v-if="checkedCount === sceneCards.length && sceneCards.length">全部完成</span>
          </div>
          <div class="progress-bar">
            <div class="progress-inner" :style="{ width: sceneCards.length ? (checkedCount/sceneCards.length*100) + '%' : '0%' }"></div>
          </div>

          <div class="cards">
            <button v-for="card in sceneCards" :key="card.id" class="check-item" :class="{ done: card.checked }" @click="toggleCheck(card)">
              <div class="check-circle">{{ card.checked ? '✓' : '' }}</div>
              <span class="card-title">{{ card.title }}</span>
              <div class="pill-dots">
                <span v-for="tag in card.tags" :key="tag.id" class="dot" :class="tag.color"></span>
              </div>
            </button>
          </div>
        </div>
      </div>

      <div v-if="currentView === 'home' && !anyModal" class="bottom-nav">
        <button class="nav-btn active">🏠<span>场景</span></button>
        <button class="fab" @click="openCreateMenu">＋</button>
        <button class="nav-btn" @click="openTagView">🏷️<span>标签</span></button>
      </div>

      <div v-if="showCreateMenu" class="modal-backdrop" @click.self="closeCreateMenu">
        <div class="modal">
          <div class="modal-header">
            <h3>新建</h3>
            <button class="icon-btn" @click="closeCreateMenu">✕</button>
          </div>
          <div class="modal-list">
            <button @click="openNewScene">新建场景<span>组合标签创建使用场景</span></button>
            <button @click="openNewCard">新建卡片<span>添加物品并打上标签</span></button>
            <button @click="openNewTag">新建标签<span>创建分类标签</span></button>
          </div>
        </div>
      </div>

      <div v-if="showNewCard" class="modal-backdrop top" @click.self="closeNewCard">
        <div class="modal">
          <div class="modal-header">
            <h3>新建卡片</h3>
            <button class="icon-btn" @click="closeNewCard">✕</button>
          </div>
          <div class="form">
            <label>卡片名称</label>
            <input v-model="newCardTitle" placeholder="如：充电宝、雨伞..." />
            <label>选择标签（可多选）</label>
            <div class="chip-list">
              <button
                v-for="tag in tags"
                :key="tag.id"
                :class="['chip', selectedNewCardTags.includes(tag.id) ? tag.color : '']"
                @click="toggleNewCardTag(tag.id)"
              >
                <span v-if="selectedNewCardTags.includes(tag.id)">✓</span>{{ tag.name }}
              </button>
            </div>
            <button class="primary" :disabled="!newCardTitle || !selectedNewCardTags.length" @click="createCard">
              创建卡片
            </button>
          </div>
        </div>
      </div>

      <div v-if="showNewScene" class="modal-backdrop top" @click.self="closeNewScene">
        <div class="modal">
          <div class="modal-header">
            <h3>新建场景</h3>
            <button class="icon-btn" @click="closeNewScene">✕</button>
          </div>
          <div class="form">
            <label>选择图标</label>
            <div class="emoji-list">
              <button
                v-for="emoji in emojiOptions"
                :key="emoji"
                :class="['emoji-btn', newSceneIcon === emoji ? 'active' : '']"
                @click="newSceneIcon = emoji"
              >
                {{ emoji }}
              </button>
            </div>
            <label>场景名称</label>
            <input v-model="newSceneName" placeholder="如：出门、旅行准备..." />
            <label>组合标签（场景=标签组合）</label>
            <div class="chip-list">
              <button
                v-for="tag in tags"
                :key="tag.id"
                :class="['chip', selectedNewSceneTags.includes(tag.id) ? tag.color : '']"
                @click="toggleNewSceneTag(tag.id)"
              >
                <span v-if="selectedNewSceneTags.includes(tag.id)">✓</span>{{ tag.name }}
              </button>
            </div>
            <label class="switch-row">
              <input type="checkbox" v-model="newScenePinned" /> 置顶到首页
            </label>
            <button class="primary" :disabled="!newSceneName || !selectedNewSceneTags.length" @click="createScene">
              创建场景
            </button>
          </div>
        </div>
      </div>

      <div v-if="showNewTag" class="modal-backdrop top" @click.self="closeNewTag">
        <div class="modal">
          <div class="modal-header">
            <h3>新建标签</h3>
            <button class="icon-btn" @click="closeNewTag">✕</button>
          </div>
          <div class="form">
            <label>标签名称</label>
            <input v-model="newTagName" placeholder="如：日常、购物清单..." />
            <label>选择颜色</label>
            <div class="color-list">
              <button
                v-for="opt in tagColorOptions"
                :key="opt.value"
                :class="['color-btn', opt.value, newTagColor === opt.value ? 'active' : '']"
                @click="newTagColor = opt.value"
              >
                {{ opt.name }}
              </button>
            </div>
            <button class="primary" :disabled="!isNewTagValid" @click="createTag">创建标签</button>
            <div v-if="newTagError" class="form-error">{{ newTagError }}</div>
          </div>
        </div>
      </div>

      <div v-if="showTagView" class="modal-backdrop" @click.self="closeTagView">
        <div class="modal full">
          <div class="modal-header">
            <h3>标签管理</h3>
            <div class="right-actions">
              <button class="new-tag-btn" @click="openNewTag">＋ 新标签</button>
              <button class="icon-btn" @click="closeTagView">✕</button>
            </div>
          </div>
          <div class="tag-filter">
            <button
              v-for="tag in visibleFilterTags"
              :key="tag.id"
              :class="['filter-chip', selectedTagFilter === tag.id ? tag.color : '']"
              @click="toggleTagFilter(tag.id)"
            >
              {{ tag.name }} <span>{{ tag.cardCount }}</span>
            </button>
            <button v-if="tags.length > 6 && !tagFilterExpanded" class="filter-chip filter-more" @click="tagFilterExpanded = true">
              +{{ tags.length - 6 }} 个标签
            </button>
            <button v-if="tagFilterExpanded && tags.length > 6" class="filter-chip filter-more" @click="tagFilterExpanded = false">
              收起
            </button>
          </div>
          <div class="list">
            <div class="list-title">{{ selectedTagFilter ? '标签下卡片' : '全部卡片' }} · {{ tagViewCards.length }} 个</div>
            <div class="list-item" v-for="card in visibleTagViewCards" :key="card.id">
              <span class="card-text-truncate">{{ card.title }}</span>
              <div class="tag-row">
                <span v-for="tag in card.tags" :key="tag.id" class="tag" :class="tag.color">{{ tag.name }}</span>
              </div>
            </div>
            <button v-if="tagViewCards.length > 6 && !tagViewExpanded" class="expand-btn" @click="tagViewExpanded = true">
              展开全部 {{ tagViewCards.length }} 条
              <span class="expand-arrow">↓</span>
            </button>
            <button v-if="tagViewExpanded && tagViewCards.length > 6" class="expand-btn" @click="tagViewExpanded = false">
              收起
              <span class="expand-arrow">↑</span>
            </button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { api } from './api';

export default {
  name: 'App',
  data() {
    return {
      currentTime: '',
      batteryLevel: 75,
      currentView: 'home',
      sceneSearch: '',
      searchResults: null,
      searchLoading: false,
      scenePinned: [],
      sceneOthers: { records: [], total: 0, page: 0, size: 8 },
      sceneOthersLoading: false,
      tags: [],
      cardsPage: { records: [], total: 0, totalPages: 0, page: 1, size: 20 },
      selectedScene: null,
      sceneCards: [],
      showCreateMenu: false,
      showNewCard: false,
      showNewScene: false,
      showNewTag: false,
      showTagView: false,
      newCardTitle: '',
      selectedNewCardTags: [],
      newSceneName: '',
      newSceneIcon: '🎒',
      selectedNewSceneTags: [],
      newScenePinned: false,
      newTagName: '',
      newTagColor: 'bg-blue-500',
      newTagError: '',
      selectedTagFilter: null,
      tagFilterExpanded: false,
      tagViewExpanded: false,
      emojiOptions: ['🚪', '✈️', '💼', '🏃', '🌴', '🎒', '🏕️', '🎉', '📌', '📦', '🎧', '🧳', '🚲', '🧼', '📷'],
      tagColorOptions: [
        { name: '蓝色', value: 'bg-blue-500' },
        { name: '紫色', value: 'bg-purple-500' },
        { name: '绿色', value: 'bg-green-500' },
        { name: '橙色', value: 'bg-orange-500' },
        { name: '粉色', value: 'bg-pink-500' },
        { name: '红色', value: 'bg-red-500' },
        { name: '青色', value: 'bg-teal-500' },
        { name: '靛蓝', value: 'bg-indigo-500' },
        { name: '黄色', value: 'bg-yellow-500' },
        { name: '灰色', value: 'bg-gray-500' }
      ]
    };
  },
  computed: {
    othersRemaining() {
      return Math.max(0, this.sceneOthers.total - this.sceneOthers.records.length);
    },
    visibleFilterTags() {
      return this.tagFilterExpanded ? this.tags : this.tags.slice(0, 6);
    },
    tagViewCards() {
      return this.cardsPage.records.filter(
        (c) => !this.selectedTagFilter || c.tags.some((t) => t.id === this.selectedTagFilter)
      );
    },
    visibleTagViewCards() {
      return this.tagViewExpanded ? this.tagViewCards : this.tagViewCards.slice(0, 6);
    },
    anyModal() {
      return this.showCreateMenu || this.showNewCard || this.showNewScene || this.showNewTag || this.showTagView;
    },
    recentCards() {
      return this.cardsPage.records.slice(0, 8);
    },
    checkedCount() {
      return this.sceneCards.filter((c) => c.checked).length;
    },
    isNewTagValid() {
      return this.newTagName.trim().length > 0;
    }
  },
  watch: {
    sceneSearch(val) {
      clearTimeout(this._searchTimer);
      if (!val.trim()) {
        this.searchResults = null;
        return;
      }
      this._searchTimer = setTimeout(() => this.doSearch(val), 300);
    }
  },
  mounted() {
    this.updateTime();
    this._timeTimer = setInterval(this.updateTime, 1000);
    this.syncBattery();
    this.refreshAll();
  },
  beforeDestroy() {
    clearInterval(this._timeTimer);
  },
  methods: {
    updateTime() {
      const now = new Date();
      const h = String(now.getHours()).padStart(2, '0');
      const m = String(now.getMinutes()).padStart(2, '0');
      this.currentTime = `${h}:${m}`;
    },
    async syncBattery() {
      if (!('getBattery' in navigator)) return;
      try {
        const battery = await navigator.getBattery();
        this.batteryLevel = Math.round(battery.level * 100);
        battery.addEventListener('levelchange', () => {
          this.batteryLevel = Math.round(battery.level * 100);
        });
      } catch (e) { /* keep default */ }
    },
    async refreshAll() {
      await Promise.all([this.fetchTags(), this.fetchScenes(1), this.fetchCards()]);
    },
    async fetchTags() {
      const res = await api.tags();
      this.tags = res.data || [];
    },
    async fetchCards(params = {}) {
      const res = await api.cards(Object.assign({ page: 1, size: 100 }, params));
      this.cardsPage = res.data || { records: [], total: 0, totalPages: 0, page: 1, size: 100 };
    },
    async fetchScenes(page = 1) {
      const res = await api.scenes({ page, size: this.sceneOthers.size });
      const data = res.data || {};
      this.scenePinned = data.pinned || [];
      if (page === 1) {
        this.sceneOthers.records = data.records || [];
      } else {
        this.sceneOthers.records = [...this.sceneOthers.records, ...(data.records || [])];
      }
      this.sceneOthers.total = data.total || 0;
      this.sceneOthers.page = data.page || page;
    },
    collapseScenes() {
      this.sceneOthers.records = this.sceneOthers.records.slice(0, this.sceneOthers.size);
      this.sceneOthers.page = 1;
    },
    async loadMoreScenes() {
      if (this.sceneOthersLoading) return;
      this.sceneOthersLoading = true;
      try {
        await this.fetchScenes(this.sceneOthers.page + 1);
      } finally {
        this.sceneOthersLoading = false;
      }
    },
    async doSearch(keyword) {
      this.searchLoading = true;
      try {
        const res = await api.scenes({ keyword, page: 1, size: 20 });
        const data = res.data || {};
        const all = [...(data.pinned || []), ...(data.records || [])];
        this.searchResults = { records: all, total: all.length };
      } finally {
        this.searchLoading = false;
      }
    },
    async openScene(scene) {
      const detail = await api.scene(scene.id);
      this.selectedScene = detail.data;
      const cardsRes = await api.sceneCards(scene.id);
      this.sceneCards = cardsRes.data || [];
      this.currentView = 'scene';
    },
    backHome() {
      this.currentView = 'home';
      this.selectedScene = null;
      this.sceneCards = [];
      this.sceneSearch = '';
      this.searchResults = null;
    },
    percent(scene) {
      if (!scene.totalCount) return 0;
      return Math.round((scene.checkedCount / scene.totalCount) * 100);
    },
    tagNames(tags) {
      return (tags || []).map((t) => t.name).join(' + ');
    },
    openCreateMenu() {
      this.showCreateMenu = true;
    },
    closeCreateMenu() {
      this.showCreateMenu = false;
    },
    openNewCard() {
      this.closeCreateMenu();
      this.showNewCard = true;
    },
    closeNewCard() {
      this.showNewCard = false;
      this.newCardTitle = '';
      this.selectedNewCardTags = [];
    },
    openNewScene() {
      this.closeCreateMenu();
      this.showNewScene = true;
    },
    closeNewScene() {
      this.showNewScene = false;
      this.newSceneName = '';
      this.selectedNewSceneTags = [];
      this.newScenePinned = false;
    },
    openNewTag() {
      this.closeCreateMenu();
      this.newTagName = '';
      this.newTagColor = 'bg-blue-500';
      this.newTagError = '';
      this.showNewTag = true;
    },
    closeNewTag() {
      this.showNewTag = false;
      this.newTagName = '';
      this.newTagError = '';
    },
    openTagView() {
      this.showTagView = true;
      this.selectedTagFilter = null;
    },
    closeTagView() {
      this.showTagView = false;
    },
    toggleNewCardTag(id) {
      const idx = this.selectedNewCardTags.indexOf(id);
      if (idx >= 0) this.selectedNewCardTags.splice(idx, 1);
      else this.selectedNewCardTags.push(id);
    },
    toggleNewSceneTag(id) {
      const idx = this.selectedNewSceneTags.indexOf(id);
      if (idx >= 0) this.selectedNewSceneTags.splice(idx, 1);
      else this.selectedNewSceneTags.push(id);
    },
    toggleTagFilter(id) {
      this.selectedTagFilter = this.selectedTagFilter === id ? null : id;
      this.tagViewExpanded = false;
    },
    async createTag() {
      const name = this.newTagName.trim();
      if (!name) {
        this.newTagError = '请输入标签名称';
        return;
      }
      this.newTagError = '';
      await api.createTag({ name, color: this.newTagColor });
      this.closeNewTag();
      await this.refreshAll();
    },
    async createCard() {
      await api.createCard({ title: this.newCardTitle, tagIds: this.selectedNewCardTags });
      this.closeNewCard();
      await this.refreshAll();
    },
    async createScene() {
      await api.createScene({
        name: this.newSceneName,
        icon: this.newSceneIcon,
        pinned: this.newScenePinned,
        tagIds: this.selectedNewSceneTags
      });
      this.closeNewScene();
      await this.fetchScenes(1);
    },
    async toggleCheck(card) {
      card.checked = !card.checked;
      await api.setCheck(this.selectedScene.id, card.id, card.checked);
      await this.fetchScenes();
    },
    async resetChecks() {
      if (!this.selectedScene) return;
      await api.resetChecks(this.selectedScene.id);
      await this.openScene({ id: this.selectedScene.id });
      await this.fetchScenes();
    },
    async togglePin(scene) {
      const payload = {
        id: scene.id,
        name: scene.name,
        icon: scene.icon,
        pinned: !scene.pinned,
        tagIds: (scene.tags || []).map((t) => t.id)
      };
      await api.updateScene(payload);
      await this.fetchScenes(1);
    }
  }
};
</script>

