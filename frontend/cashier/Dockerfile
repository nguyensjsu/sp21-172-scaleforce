FROM node:14.16.1-alpine
WORKDIR /app
ENV PATH /app/node_modules_/.bin:$PATH
COPY package.json ./
COPY yarn.lock ./
RUN yarn install
COPY . ./
CMD ["yarn", "start"]