<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>代齐 README.md</title>
<style>
  * { margin: 0; padding: 0; box-sizing: border-box; }
  body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    background: #0d1117;
    color: #c9d1d9;
    padding: 20px;
    line-height: 1.7;
  }
  .toolbar {
    position: sticky;
    top: 0;
    z-index: 100;
    background: #161b22;
    border: 1px solid #30363d;
    border-radius: 10px;
    padding: 16px 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    flex-wrap: wrap;
    gap: 12px;
  }
  .toolbar-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  .toolbar-left span {
    font-size: 18px;
    font-weight: 600;
    color: #e6edf3;
  }
  .file-icon {
    width: 32px; height: 32px;
    background: #238636;
    border-radius: 6px;
    display: flex; align-items: center; justify-content: center;
    font-size: 16px; color: #fff; font-weight: bold;
  }
  .btn-group { display: flex; gap: 10px; flex-wrap: wrap; }
  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  .btn-primary {
    background: #238636;
    color: #fff;
  }
  .btn-primary:hover { background: #2ea043; }
  .btn-secondary {
    background: #21262d;
    color: #c9d1d9;
    border: 1px solid #30363d;
  }
  .btn-secondary:hover { background: #30363d; }
  .btn svg { width: 16px; height: 16px; }

  .toast {
    position: fixed;
    top: 24px;
    left: 50%;
    transform: translateX(-50%) translateY(-100px);
    background: #238636;
    color: #fff;
    padding: 12px 28px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    transition: transform 0.3s ease;
    z-index: 999;
  }
  .toast.show {
    transform: translateX(-50%) translateY(0);
  }

  .preview {
    background: #161b22;
    border: 1px solid #30363d;
    border-radius: 10px;
    padding: 32px 40px;
    max-width: 900px;
    margin: 0 auto;
    overflow-x: auto;
  }
  @media (max-width: 600px) {
    .preview { padding: 20px 16px; }
    .toolbar { padding: 12px 16px; }
  }

  /* Markdown-like styling */
  .preview h1 { font-size: 28px; color: #e6edf3; border-bottom: 1px solid #30363d; padding-bottom: 12px; margin-bottom: 20px; }
  .preview h2 { font-size: 22px; color: #e6edf3; border-bottom: 1px solid #21262d; padding-bottom: 8px; margin: 32px 0 16px; }
  .preview h3 { font-size: 17px; color: #e6edf3; margin: 24px 0 12px; }
  .preview p { margin: 10px 0; }
  .preview blockquote {
    border-left: 4px solid #3fb950;
    padding: 8px 16px;
    margin: 16px 0;
    color: #8b949e;
    background: #0d1117;
    border-radius: 0 6px 6px 0;
  }
  .preview code {
    background: #0d1117;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 13px;
    color: #79c0ff;
    font-family: 'SFMono-Regular', Consolas, monospace;
  }
  .preview pre {
    background: #0d1117;
    border: 1px solid #30363d;
    border-radius: 8px;
    padding: 16px;
    overflow-x: auto;
    margin: 16px 0;
  }
  .preview pre code {
    padding: 0;
    background: none;
    color: #c9d1d9;
    font-size: 13px;
    line-height: 1.6;
  }
  .preview table {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;
    font-size: 14px;
  }
  .preview th {
    background: #21262d;
    color: #e6edf3;
    padding: 10px 14px;
    text-align: left;
    border: 1px solid #30363d;
    font-weight: 600;
  }
  .preview td {
    padding: 8px 14px;
    border: 1px solid #30363d;
  }
  .preview tr:hover td { background: #161b2288; }
  .preview ul, .preview ol { padding-left: 24px; margin: 10px 0; }
  .preview li { margin: 4px 0; }
  .preview hr { border: none; border-top: 1px solid #30363d; margin: 24px 0; }
  .preview strong { color: #e6edf3; }
  .preview a { color: #58a6ff; text-decoration: none; }
  .badge-row { display: flex; gap: 8px; flex-wrap: wrap; margin: 12px 0 20px; }
  .badge {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 600;
  }
  .badge-vue { background: #4FC08D22; color: #4FC08D; border: 1px solid #4FC08D44; }
  .badge-axios { background: #5A29E422; color: #a78bfa; border: 1px solid #5A29E444; }
  .badge-mit { background: #2563eb22; color: #60a5fa; border: 1px solid #2563eb44; }
</style>
</head>
<body>

<div id="toast" class="toast">✅ 已复制到剪贴板</div>

<div class="toolbar">
  <div class="toolbar-left">
    <div class="file-icon">M</div>
    <span>README.md</span>
  </div>
  <div class="btn-group">
    <button class="btn btn-secondary" onclick="copyRaw()">
      <svg fill="currentColor" viewBox="0 0 16 16"><path d="M0 6.75C0 5.784.784 5 1.75 5h1.5a.75.75 0 0 1 0 1.5h-1.5a.25.25 0 0 0-.25.25v7.5c0 .138.112.25.25.25h7.5a.25.25 0 0 0 .25-.25v-1.5a.75.75 0 0 1 1.5 0v1.5A1.75 1.75 0 0 1 9.25 16h-7.5A1.75 1.75 0 0 1 0 14.25Z"></path><path d="M5 1.75C5 .784 5.784 0 6.75 0h7.5C15.216 0 16 .784 16 1.75v7.5A1.75 1.75 0 0 1 14.25 11h-7.5A1.75 1.75 0 0 1 5 9.25Zm1.75-.25a.25.25 0 0 0-.25.25v7.5c0 .138.112.25.25.25h7.5a.25.25 0 0 0 .25-.25v-7.5a.25.25 0 0 0-.25-.25Z"></path></svg>
      复制源码
    </button>
    <button class="btn btn-primary" onclick="downloadFile()">
      <svg fill="currentColor" viewBox="0 0 16 16"><path d="M2.75 14A1.75 1.75 0 0 1 1 12.25v-2.5a.75.75 0 0 1 1.5 0v2.5c0 .138.112.25.25.25h10.5a.25.25 0 0 0 .25-.25v-2.5a.75.75 0 0 1 1.5 0v2.5A1.75 1.75 0 0 1 13.25 14Z"></path><path d="M7.25 7.689V2a.75.75 0 0 1 1.5 0v5.689l1.97-1.969a.749.749 0 1 1 1.06 1.06l-3.25 3.25a.749.749 0 0 1-1.06 0L4.22 6.78a.749.749 0 1 1 1.06-1.06l1.97 1.969Z"></path></svg>
      下载 README.md
    </button>
  </div>
</div>

<div class="preview">
  <h1>🧳 代齐 —— 场景化标签清单工具</h1>
  <blockquote>用标签组合场景，告别层级结构。一款面向个人的场景化物品确认清单应用。</blockquote>
  <div class="badge-row">
    <span class="badge badge-vue">Vue 2.7</span>
    <span class="badge badge-axios">Axios 1.6</span>
    <span class="badge badge-mit">MIT License</span>
  </div>
  <hr>

  <h2>📖 项目简介</h2>
  <p><strong>代齐</strong> 是一款场景化标签清单管理工具，核心理念是：</p>
  <ul>
    <li><strong>标签</strong> 是最小的分类单位（如：日常、出差、运动）</li>
    <li><strong>卡片</strong> 是具体的物品/事项（如：充电宝、身份证、雨伞）</li>
    <li><strong>场景</strong> 是标签的组合（如："出门" = 日常 + 随身）</li>
  </ul>
  <p>通过标签自由组合场景，每个场景自动关联对应标签下的所有卡片，进入场景后逐一确认，实现出门前的快速检查。</p>
  <hr>

  <h2>🎯 核心功能</h2>

  <h3>场景管理</h3>
  <ul>
    <li>创建场景（选择图标 + 名称 + 组合标签）</li>
    <li>场景置顶 / 取消置顶</li>
    <li>进入场景查看关联卡片</li>
    <li>逐项确认（打勾），支持进度展示</li>
    <li>一键重置所有确认状态</li>
  </ul>

  <h3>卡片管理</h3>
  <ul>
    <li>创建卡片（名称 + 多标签）</li>
    <li>卡片自动归属到含相同标签的场景中</li>
    <li>首页展示最近卡片</li>
  </ul>

  <h3>标签管理</h3>
  <ul>
    <li>创建标签（名称 + 颜色）</li>
    <li>标签筛选查看关联卡片</li>
    <li>10 种预设颜色可选</li>
  </ul>
  <hr>

  <h2>📱 产品界面</h2>
  <p>应用采用深色主题 Mobile-First 设计，模拟 375×812 手机视口。</p>
  <pre><code>┌─────────────────────────┐
│      状态栏 9:41         │
├─────────────────────────┤
│  场景                🏷️ │
│  用标签组合场景...        │
│                         │
│  ● 置顶场景              │
│  ┌─────┐ ┌─────┐       │
│  │🎒出门│ │✈️旅行│       │
│  │ 3/5  │ │ 0/8  │       │
│  └─────┘ └─────┘       │
│                         │
│  ○ 其他场景              │
│  ┌───────────────────┐  │
│  │💼 上班  日常+通勤    │  │
│  │🏃 运动  运动+随身    │  │
│  └───────────────────┘  │
│                         │
│  ○ 最近卡片              │
│  [充电宝] [身份证] [钥匙]│
│                         │
├─────────────────────────┤
│  🏠场景    ＋    🏷️标签  │
└─────────────────────────┘</code></pre>
  <hr>

  <h2>🏗️ 技术架构</h2>
  <pre><code>daiqi-frontend/
├── public/
│   └── index.html
├── src/
│   ├── main.js            # 应用入口
│   ├── App.vue            # 根组件（单页应用，所有视图）
│   ├── api.js             # API 接口封装（Axios）
│   └── assets/
│       └── styles.css     # 全局样式（深色主题）
├── package.json
└── vue.config.js</code></pre>

  <table>
    <tr><th>层级</th><th>技术选型</th><th>说明</th></tr>
    <tr><td>框架</td><td>Vue 2.7</td><td>单文件组件</td></tr>
    <tr><td>HTTP</td><td>Axios</td><td>POST 风格 REST API</td></tr>
    <tr><td>样式</td><td>原生 CSS</td><td>CSS 变量 + 深色主题</td></tr>
    <tr><td>构建</td><td>Vue CLI</td><td>Webpack 打包</td></tr>
    <tr><td>设计</td><td>Mobile-First</td><td>375×812 手机模拟</td></tr>
  </table>
  <hr>

  <h2>📡 API 接口</h2>
  <p>所有接口均为 <strong>POST</strong> 请求，请求体和响应体均为 JSON。</p>

  <h3>标签（Tags）</h3>
  <table>
    <tr><th>接口</th><th>说明</th><th>请求参数</th></tr>
    <tr><td><code>/api/tags/list</code></td><td>标签列表</td><td><code>{}</code></td></tr>
    <tr><td><code>/api/tags/create</code></td><td>创建标签</td><td><code>{ name, color }</code></td></tr>
    <tr><td><code>/api/tags/update</code></td><td>更新标签</td><td><code>{ id, name, color }</code></td></tr>
    <tr><td><code>/api/tags/delete</code></td><td>删除标签</td><td><code>{ id }</code></td></tr>
  </table>

  <h3>卡片（Cards）</h3>
  <table>
    <tr><th>接口</th><th>说明</th><th>请求参数</th></tr>
    <tr><td><code>/api/cards/list</code></td><td>卡片列表</td><td><code>{ page?, size? }</code></td></tr>
    <tr><td><code>/api/cards/create</code></td><td>创建卡片</td><td><code>{ title, tagIds[] }</code></td></tr>
    <tr><td><code>/api/cards/update</code></td><td>更新卡片</td><td><code>{ id, title, tagIds[] }</code></td></tr>
    <tr><td><code>/api/cards/delete</code></td><td>删除卡片</td><td><code>{ id }</code></td></tr>
  </table>

  <h3>场景（Scenes）</h3>
  <table>
    <tr><th>接口</th><th>说明</th><th>请求参数</th></tr>
    <tr><td><code>/api/scenes/list</code></td><td>场景列表</td><td><code>{}</code></td></tr>
    <tr><td><code>/api/scenes/get</code></td><td>场景详情</td><td><code>{ id }</code></td></tr>
    <tr><td><code>/api/scenes/cards</code></td><td>场景下卡片</td><td><code>{ id }</code></td></tr>
    <tr><td><code>/api/scenes/create</code></td><td>创建场景</td><td><code>{ name, icon, pinned, tagIds[] }</code></td></tr>
    <tr><td><code>/api/scenes/update</code></td><td>更新场景</td><td><code>{ id, name, icon, pinned, tagIds[] }</code></td></tr>
    <tr><td><code>/api/scenes/delete</code></td><td>删除场景</td><td><code>{ id }</code></td></tr>
    <tr><td><code>/api/scenes/check</code></td><td>确认/取消项</td><td><code>{ sceneId, cardId, checked }</code></td></tr>
    <tr><td><code>/api/scenes/reset</code></td><td>重置全部确认</td><td><code>{ id }</code></td></tr>
  </table>
  <hr>

  <h2>🚀 快速开始</h2>

  <h3>环境要求</h3>
  <ul>
    <li>Node.js >= 14</li>
    <li>npm >= 6（或 yarn >= 1.22）</li>
  </ul>

  <h3>安装依赖</h3>
  <pre><code>npm install</code></pre>

  <h3>本地开发</h3>
  <pre><code>npm run serve</code></pre>
  <p>访问 <code>http://localhost:8080</code></p>

  <h3>构建生产包</h3>
  <pre><code>npm run build</code></pre>
  <p>产物输出到 <code>dist/</code> 目录。</p>

  <h3>代理配置</h3>
  <p>开发环境如需代理后端接口，在 <code>vue.config.js</code> 中配置：</p>
  <pre><code>module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
};</code></pre>
  <hr>

  <h2>📊 数据模型</h2>
  <pre><code>┌──────────┐     N:N      ┌──────────┐
│   Tag    │◄────────────►│   Card   │
│──────────│              │──────────│
│ id       │              │ id       │
│ name     │              │ title    │
│ color    │              │ tags[]   │
│ cardCount│              └──────────┘
└──────────┘                    ▲
      ▲                         │
      │ N:N                     │ 关联
      ▼                         │
┌──────────┐              ┌──────────┐
│  Scene   │─────────────►│SceneCard │
│──────────│   1:N        │──────────│
│ id       │              │ sceneId  │
│ name     │              │ cardId   │
│ icon     │              │ checked  │
│ pinned   │              └──────────┘
│ tags[]   │
│ checkedCount │
│ totalCount   │
└──────────┘</code></pre>
  <p><strong>核心关系：</strong></p>
  <ul>
    <li>一个标签可以关联多张卡片，一张卡片可以有多个标签</li>
    <li>一个场景由多个标签组合而成</li>
    <li>场景下的卡片 = 含有场景内任一标签的所有卡片</li>
    <li>每个场景独立维护每张卡片的确认状态</li>
  </ul>
  <hr>

  <h2>🎨 设计规范</h2>

  <h3>色彩系统</h3>
  <table>
    <tr><th>变量</th><th>色值</th><th>用途</th></tr>
    <tr><td><code>--bg</code></td><td><code>#0b0f14</code></td><td>页面背景</td></tr>
    <tr><td><code>--panel</code></td><td><code>#111827</code></td><td>面板/卡片</td></tr>
    <tr><td><code>--text</code></td><td><code>#e5e7eb</code></td><td>主文字</td></tr>
    <tr><td><code>--muted</code></td><td><code>#9ca3af</code></td><td>次要文字</td></tr>
    <tr><td><code>--blue</code></td><td><code>#2563eb</code></td><td>主色/按钮</td></tr>
    <tr><td><code>--green</code></td><td><code>#16a34a</code></td><td>成功/已完成</td></tr>
    <tr><td><code>--yellow</code></td><td><code>#f59e0b</code></td><td>高亮/置顶</td></tr>
  </table>

  <h3>标签颜色</h3>
  <p>提供 10 种预设背景色：蓝、紫、绿、橙、粉、红、青、靛蓝、黄、灰</p>

  <h3>图标预设</h3>
  <p>场景图标从 15 个 Emoji 中选择：🚪 ✈️ 💼 🏃 🌴 🎒 🏕️ 🎉 📌 📦 🎧 🧳 🚲 🧼 📷</p>
  <hr>

  <h2>🔄 版本记录</h2>
  <h3>v1.0.0</h3>
  <ul>
    <li>✅ 场景 CRUD 及置顶</li>
    <li>✅ 卡片 CRUD 及多标签</li>
    <li>✅ 标签 CRUD 及颜色选择</li>
    <li>✅ 场景内逐项确认与进度</li>
    <li>✅ 一键重置确认状态</li>
    <li>✅ 标签管理筛选视图</li>
    <li>✅ 深色主题 UI</li>
  </ul>
  <hr>

  <h2>📝 License</h2>
  <p>MIT</p>
</div>

<script>
const readmeContent = `# 🧳 代齐 —— 场景化标签清单工具

> 用标签组合场景，告别层级结构。一款面向个人的场景化物品确认清单应用。

![Vue](https://img.shields.io/badge/Vue-2.7-4FC08D?logo=vue.js)
![Axios](https://img.shields.io/badge/Axios-1.6-5A29E4)
![License](https://img.shields.io/badge/License-MIT-blue)

---

## 📖 项目简介

**代齐** 是一款场景化标签清单管理工具，核心理念是：

- **标签** 是最小的分类单位（如：日常、出差、运动）
- **卡片** 是具体的物品/事项（如：充电宝、身份证、雨伞）
- **场景** 是标签的组合（如："出门" = 日常 + 随身）

通过标签自由组合场景，每个场景自动关联对应标签下的所有卡片，进入场景后逐一确认，实现出门前的快速检查。

---

## 🎯 核心功能

### 场景管理

- 创建场景（选择图标 + 名称 + 组合标签）
- 场景置顶 / 取消置顶
- 进入场景查看关联卡片
- 逐项确认（打勾），支持进度展示
- 一键重置所有确认状态

### 卡片管理

- 创建卡片（名称 + 多标签）
- 卡片自动归属到含相同标签的场景中
- 首页展示最近卡片

### 标签管理

- 创建标签（名称 + 颜色）
- 标签筛选查看关联卡片
- 10 种预设颜色可选

---

## 📱 产品界面

应用采用深色主题 Mobile-First 设计，模拟 375×812 手机视口。

\`\`\`
┌─────────────────────────┐
│      状态栏 9:41         │
├─────────────────────────┤
│  场景                🏷️ │
│  用标签组合场景...        │
│                         │
│  ● 置顶场景              │
│  ┌─────┐ ┌─────┐       │
│  │🎒出门│ │✈️旅行│       │
│  │ 3/5  │ │ 0/8  │       │
│  └─────┘ └─────┘       │
│                         │
│  ○ 其他场景              │
│  ┌───────────────────┐  │
│  │💼 上班  日常+通勤    │  │
│  │🏃 运动  运动+随身    │  │
│  └───────────────────┘  │
│                         │
│  ○ 最近卡片              │
│  [充电宝] [身份证] [钥匙]│
│                         │
├─────────────────────────┤
│  🏠场景    ＋    🏷️标签  │
└─────────────────────────┘
\`\`\`

---

## 🏗️ 技术架构

\`\`\`
daiqi-frontend/
├── public/
│   └── index.html
├── src/
│   ├── main.js            # 应用入口
│   ├── App.vue            # 根组件（单页应用，所有视图）
│   ├── api.js             # API 接口封装（Axios）
│   └── assets/
│       └── styles.css     # 全局样式（深色主题）
├── package.json
└── vue.config.js
\`\`\`

| 层级 | 技术选型 | 说明 |
|------|---------|------|
| 框架 | Vue 2.7 | 单文件组件 |
| HTTP | Axios | POST 风格 REST API |
| 样式 | 原生 CSS | CSS 变量 + 深色主题 |
| 构建 | Vue CLI | Webpack 打包 |
| 设计 | Mobile-First | 375×812 手机模拟 |

---

## 📡 API 接口

所有接口均为 **POST** 请求，请求体和响应体均为 JSON。

### 标签（Tags）

| 接口 | 说明 | 请求参数 |
|------|------|---------|
| \`/api/tags/list\` | 标签列表 | \`{}\` |
| \`/api/tags/create\` | 创建标签 | \`{ name, color }\` |
| \`/api/tags/update\` | 更新标签 | \`{ id, name, color }\` |
| \`/api/tags/delete\` | 删除标签 | \`{ id }\` |

### 卡片（Cards）

| 接口 | 说明 | 请求参数 |
|------|------|---------|
| \`/api/cards/list\` | 卡片列表 | \`{ page?, size? }\` |
| \`/api/cards/create\` | 创建卡片 | \`{ title, tagIds[] }\` |
| \`/api/cards/update\` | 更新卡片 | \`{ id, title, tagIds[] }\` |
| \`/api/cards/delete\` | 删除卡片 | \`{ id }\` |

### 场景（Scenes）

| 接口 | 说明 | 请求参数 |
|------|------|---------|
| \`/api/scenes/list\` | 场景列表 | \`{}\` |
| \`/api/scenes/get\` | 场景详情 | \`{ id }\` |
| \`/api/scenes/cards\` | 场景下卡片 | \`{ id }\` |
| \`/api/scenes/create\` | 创建场景 | \`{ name, icon, pinned, tagIds[] }\` |
| \`/api/scenes/update\` | 更新场景 | \`{ id, name, icon, pinned, tagIds[] }\` |
| \`/api/scenes/delete\` | 删除场景 | \`{ id }\` |
| \`/api/scenes/check\` | 确认/取消项 | \`{ sceneId, cardId, checked }\` |
| \`/api/scenes/reset\` | 重置全部确认 | \`{ id }\` |

---

## 🚀 快速开始

### 环境要求

- Node.js >= 14
- npm >= 6（或 yarn >= 1.22）

### 安装依赖

\`\`\`bash
npm install
\`\`\`

### 本地开发

\`\`\`bash
npm run serve
\`\`\`

访问 \`http://localhost:8080\`

### 构建生产包

\`\`\`bash
npm run build
\`\`\`

产物输出到 \`dist/\` 目录。

### 代理配置

开发环境如需代理后端接口，在 \`vue.config.js\` 中配置：

\`\`\`js
module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
};
\`\`\`

---

## 📊 数据模型

\`\`\`
┌──────────┐     N:N      ┌──────────┐
│   Tag    │◄────────────►│   Card   │
│──────────│              │──────────│
│ id       │              │ id       │
│ name     │              │ title    │
│ color    │              │ tags[]   │
│ cardCount│              └──────────┘
└──────────┘                    ▲
      ▲                         │
      │ N:N                     │ 关联
      ▼                         │
┌──────────┐              ┌──────────┐
│  Scene   │─────────────►│SceneCard │
│──────────│   1:N        │──────────│
│ id       │              │ sceneId  │
│ name     │              │ cardId   │
│ icon     │              │ checked  │
│ pinned   │              └──────────┘
│ tags[]   │
│ checkedCount │
│ totalCount   │
└──────────┘
\`\`\`

**核心关系：**

- 一个标签可以关联多张卡片，一张卡片可以有多个标签
- 一个场景由多个标签组合而成
- 场景下的卡片 = 含有场景内任一标签的所有卡片
- 每个场景独立维护每张卡片的确认状态

---

## 🎨 设计规范

### 色彩系统

| 变量 | 色值 | 用途 |
|------|------|------|
| \`--bg\` | \`#0b0f14\` | 页面背景 |
| \`--panel\` | \`#111827\` | 面板/卡片 |
| \`--text\` | \`#e5e7eb\` | 主文字 |
| \`--muted\` | \`#9ca3af\` | 次要文字 |
| \`--blue\` | \`#2563eb\` | 主色/按钮 |
| \`--green\` | \`#16a34a\` | 成功/已完成 |
| \`--yellow\` | \`#f59e0b\` | 高亮/置顶 |

### 标签颜色

提供 10 种预设背景色：蓝、紫、绿、橙、粉、红、青、靛蓝、黄、灰

### 图标预设

场景图标从 15 个 Emoji 中选择：🚪 ✈️ 💼 🏃 🌴 🎒 🏕️ 🎉 📌 📦 🎧 🧳 🚲 🧼 📷

---

## 🔄 版本记录

### v1.0.0

- ✅ 场景 CRUD 及置顶
- ✅ 卡片 CRUD 及多标签
- ✅ 标签 CRUD 及颜色选择
- ✅ 场景内逐项确认与进度
- ✅ 一键重置确认状态
- ✅ 标签管理筛选视图
- ✅ 深色主题 UI

---

## 📝 License

MIT
`;

function showToast(msg) {
  const toast = document.getElementById('toast');
  toast.textContent = msg;
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 2000);
}

function copyRaw() {
  navigator.clipboard.writeText(readmeContent).then(() => {
    showToast('✅ 已复制到剪贴板');
  }).catch(() => {
    // fallback
    const ta = document.createElement('textarea');
    ta.value = readmeContent;
    document.body.appendChild(ta);
    ta.select();
    document.execCommand('copy');
    document.body.removeChild(ta);
    showToast('✅ 已复制到剪贴板');
  });
}

function downloadFile() {
  const blob = new Blob([readmeContent], { type: 'text/markdown;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'README.md';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
  showToast('📥 下载已开始');
}
</script>

</body>
</html>
