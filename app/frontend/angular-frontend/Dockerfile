FROM node:latest as builder

WORKDIR /usr/local/app

COPY ./ /usr/local/app/

RUN npm install

RUN npm run build --prod


# Stage 2: Serve app with nginx server

FROM nginx:latest

COPY --from=builder /usr/local/app/dist/angular-frontend /usr/share/nginx/html

EXPOSE 80
