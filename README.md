<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">



<div class="preview">
  <h1>🧳 带齐 —— 场景化标签清单工具</h1>
  <blockquote>用标签组合场景，告别层级结构。一款面向个人的场景化物品确认清单应用。</blockquote>
  <div class="badge-row">
    <span class="badge badge-vue">Vue 2.7</span>
    <span class="badge badge-axios">Axios 1.6</span>
    <span class="badge badge-mit">MIT License</span>
  </div>
  <hr>

  <h2>📖 项目简介</h2>
  <p><strong>带齐</strong> 是一款场景化标签清单管理工具，核心理念是：</p>
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


</div>



</body>
</html>
