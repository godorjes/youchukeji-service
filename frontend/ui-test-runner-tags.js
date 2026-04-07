const fs = require('fs');
const path = require('path');
const { chromium } = require('playwright');

const BASE_URL = 'http://localhost:8080';
const REPORT_PATH = path.resolve(__dirname, '..', 'ui-test-report-tags-2026-04-08.md');

function nowDate() {
  const d = new Date();
  const pad = (n) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
}

async function ensureHome(page) {
  const close = async (selector) => {
    const loc = page.locator(selector);
    if (await loc.count()) await loc.first().click();
  };
  await close('.modal .icon-btn:has-text("✕")');
  await close('.modal.full .icon-btn:has-text("✕")');
  await close('.modal-backdrop');
  if (await page.locator('.scene-header .icon-btn:has-text("‹")').count()) {
    await page.click('.scene-header .icon-btn:has-text("‹")');
  }
  await page.waitForSelector('.bottom-nav');
}

(async () => {
  const results = new Map();
  const setResult = (id, status, note) => results.set(id, { status, note });

  // clean existing tag for deterministic TAG-006
  try {
    const res = await fetch(BASE_URL + '/api/tags/list', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: '{}'
    });
    const tags = await res.json();
    const existing = (tags || []).find(t => t.name === '购物清单');
    if (existing) {
      const del = await fetch(BASE_URL + '/api/tags/delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: existing.id })
      });
      if (!del.ok) {
        console.warn('Failed to delete existing 购物清单 tag:', del.status);
      }
    }
  } catch (e) {
    console.warn('Pre-clean failed:', e.message);
  }

  let browser;
  try {
    browser = await chromium.launch({ channel: 'chrome' });
  } catch (e) {
    browser = await chromium.launch();
  }
  const page = await browser.newPage({ viewport: { width: 420, height: 860 } });

  try {
    await page.goto(BASE_URL, { waitUntil: 'networkidle' });
    await page.waitForSelector('text=场景');

    // Open tag manager
    await page.click('.bottom-nav .nav-btn:has-text("标签")');
    await page.waitForSelector('.modal.full:has-text("标签管理")');

    // Open new tag modal
    await page.click('.modal.full .new-tag-btn');
    await page.waitForSelector('.modal:has-text("新建标签")');

    // TAG-004: switch color to orange
    try {
      await page.click('.color-list .color-btn:has-text("橙色")');
      await page.waitForTimeout(100);
      const isActive = await page.locator('.color-list .color-btn:has-text("橙色")').evaluate(el => el.classList.contains('active'));
      if (isActive) setResult('TAG-004', 'PASS', '橙色选中');
      else setResult('TAG-004', 'FAIL', '橙色未选中');
    } catch (e) {
      setResult('TAG-004', 'FAIL', '切换颜色失败');
    }

    // TAG-006: create tag
    try {
      await page.fill('.modal:has-text("新建标签") input', '购物清单');
      await page.click('.modal:has-text("新建标签") .primary');
      await page.waitForSelector('.modal.full .tag-filter');
      const exists = await page.locator('.filter-chip', { hasText: '购物清单' }).count();
      if (exists) setResult('TAG-006', 'PASS', '创建成功');
      else setResult('TAG-006', 'FAIL', '未出现在列表');
    } catch (e) {
      setResult('TAG-006', 'FAIL', '创建失败');
    }

    // TAG-007: empty name should disable
    try {
      await page.click('.modal.full .new-tag-btn');
      const disabled = await page.locator('.modal:has-text("新建标签") .primary').isDisabled();
      if (disabled) setResult('TAG-007', 'PASS', '空名称禁用');
      else setResult('TAG-007', 'FAIL', '空名称未禁用');
      await page.click('.modal:has-text("新建标签") .icon-btn:has-text("✕")');
      await ensureHome(page);
    } catch (e) {
      setResult('TAG-007', 'FAIL', '检测失败');
    }
  } finally {
    await browser.close();
  }

  const lines = [];
  lines.push('# UI 自动化测试报告（仅 TAG-004/006/007）');
  lines.push('');
  lines.push(`日期：${nowDate()}`);
  lines.push(`地址：${BASE_URL}`);
  lines.push('');
  lines.push('| 用例编号 | 状态 | 备注 |');
  lines.push('| --- | --- | --- |');
  for (const id of ['TAG-004','TAG-006','TAG-007']) {
    const r = results.get(id) || { status: 'SKIP', note: '未执行' };
    lines.push(`| ${id} | ${r.status} | ${r.note} |`);
  }
  const content = '\ufeff' + lines.join('\n');
  fs.writeFileSync(REPORT_PATH, content, 'utf8');
  console.log('Report:', REPORT_PATH);
})();
