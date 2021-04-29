FROM node:14.16.1-alpine
WORKDIR /app
ENV PATH /app/node_modules_/.bin:$PATH
COPY package.json ./
COPY package-lock.json ./
RUN npm ci
RUN npm install
COPY . ./
CMD ["npm", "start"]