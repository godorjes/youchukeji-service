const { chromium } = require('playwright');
(async () => {
  let browser;
  try { browser = await chromium.launch({ channel: 'chrome' }); } catch { browser = await chromium.launch(); }
  const page = await browser.newPage({ viewport: { width: 420, height: 860 } });
  await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
  const srcs = await page.evaluate(() => Array.from(document.querySelectorAll('script[src]')).map(s => s.getAttribute('src')));
  console.log(srcs);
  await browser.close();
})();
