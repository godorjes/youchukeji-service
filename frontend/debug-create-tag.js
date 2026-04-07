const { chromium } = require('playwright');
(async () => {
  let browser;
  try { browser = await chromium.launch({ channel: 'chrome' }); } catch { browser = await chromium.launch(); }
  const page = await browser.newPage({ viewport: { width: 420, height: 860 } });
  await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
  await page.click('.bottom-nav .nav-btn:has-text("标签")');
  await page.waitForSelector('.modal.full:has-text("标签管理")');
  await page.click('.modal.full .new-tag-btn');
  await page.waitForSelector('.modal:has-text("新建标签")');
  await page.fill('.modal:has-text("新建标签") input', '购物清单');
  const disabled = await page.locator('.modal:has-text("新建标签") .primary').isDisabled();
  console.log('disabled:', disabled);
  await page.click('.modal:has-text("新建标签") .primary', { force: true });
  await page.waitForTimeout(500);
  const exists = await page.locator('.filter-chip', { hasText: '购物清单' }).count();
  console.log('exists after create:', exists);
  await browser.close();
})();
