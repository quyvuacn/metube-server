FROM node:21-alpine

WORKDIR /usr/app

ARG NODE_ENV
ENV NODE_ENV $NODE_ENV

COPY package*.json /usr/app/
RUN npm install

COPY . /usr/app

CMD [ "npm", "start" ]
