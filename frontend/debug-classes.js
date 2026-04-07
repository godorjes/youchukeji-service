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
  const classes = await page.evaluate(() => Array.from(document.querySelectorAll('.modal-backdrop')).map(el => el.className));
  console.log(classes);
  await browser.close();
})();
