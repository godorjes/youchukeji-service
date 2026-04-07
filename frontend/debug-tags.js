const { chromium } = require('playwright');
const fs = require('fs');
(async () => {
  let browser;
  try { browser = await chromium.launch({ channel: 'chrome' }); } catch { browser = await chromium.launch(); }
  const page = await browser.newPage({ viewport: { width: 420, height: 860 } });
  await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
  await page.click('.bottom-nav .nav-btn:has-text("标签")');
  await page.waitForSelector('.modal.full:has-text("标签管理")');
  await page.click('.modal.full .new-tag-btn');
  await page.waitForSelector('.modal:has-text("新建标签")');
  const texts = await page.locator('.color-list .color-btn').allTextContents();
  console.log('color buttons:', texts);
  await page.click('.color-list .color-btn:has-text("橙色")');
  const classes = await page.locator('.color-list .color-btn:has-text("橙色")').evaluate(el => el.className);
  console.log('orange class:', classes);
  await page.screenshot({ path: 'F:\\code\\daiqi\\debug-tag.png', fullPage: true });
  await browser.close();
})();
