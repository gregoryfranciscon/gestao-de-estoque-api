const http = require("http");
const fs = require("fs");
const path = require("path");

const base = process.cwd();
const port = 5500;

const mime = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".svg": "image/svg+xml",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".jpeg": "image/jpeg",
  ".ico": "image/x-icon",
  ".woff": "font/woff",
  ".woff2": "font/woff2"
};

const server = http.createServer((req, res) => {
  let reqPath = decodeURIComponent((req.url || "/").split("?")[0]);
  if (reqPath === "/") reqPath = "/index.html";

  const filePath = path.join(base, reqPath.replace(/^\/+/, ""));

  if (!filePath.startsWith(base)) {
    res.statusCode = 403;
    res.end("Forbidden");
    return;
  }

  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.statusCode = 404;
      res.end("Not found");
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    res.setHeader("Content-Type", mime[ext] || "application/octet-stream");
    res.end(data);
  });
});

server.listen(port, () => {
  console.log(`Frontend rodando em http://localhost:${port}`);
});
