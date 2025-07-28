FROM node:22 AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM node:22-slim
WORKDIR /app

COPY --from=builder /app/package*.json ./
COPY --from=builder /app/artifacts/dist ./dist
COPY --from=builder /app/node_modules ./node_modules

CMD ["node", "dist/src/index.js"]