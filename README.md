# Introduction

This repo is just a test repo that I'm showing publicly. I'm just having some fun, learning a few things, and building myself some quick reference material.

Expect this repo to be full of contrived stuff.  It's purely for play and to show a little of my code style because all of my coding history is private.

## Setup

### Development

Development requires Keycloak for auth purposes (JWT tokens, etc).

#### Keycloak

1. Create the docker image.

   `
   /path-to-app-root/>docker build -t keycloak-test -f Dockerfile-keycloak
   `

2. Spin up the docker image.

   `
   cmd>docker run -dp 8008:8080 keycloak-test start-dev
   `

3. If no realm file, or realm changes needed, log into keycloak.

   Navigate to http://localhost:8008. Use username "admin" and password "password".  (This is a dev instance running locally).
4. Follow this guide:  https://www.baeldung.com/spring-boot-keycloak#creating-a-keycloak-realm
5. To export a new realm file, keycloak must not be running. The entrypoint must be changed to make it not run on boot.  Run the following in a terminal.
   
   Find the container ID (can be done in docker desktop as well)
   
   `
   cmd>docker ps -a
   `
   
   Commit the stopped container to create a new image:

   `
   cmd>docker commit $container-id keycloak-dump
   `
   
   Start with a new entrypoint:

   `
   cmd>docker run -ti -p 8008:8080 --entrypoint=sh keycloak-dump
   `

   You can now execute the dump command in the above tutorial.  Download the file from the /tmp/keycloak folder in the files tab in Docker Desktop.

6. Modify the Dockerfile-keycloak file with the new realm file in the project (or simply replace todo-keycloak-realm.json with your own file). Run through steps 1 and 2 again.

#### Architecture

To access the app, the following must be running:

1. Keycloak in a docker image, listening on port 8008.
2. todo-gateway, running on port 8080 (default port).
3. The vue app in todo-core-fe, running on port 4000 (default port).
4. todo-core, running on port 8081 (default port).

The todo-gateway module acts as both a gateway and a reverse proxy. All requests are directed at http://localhost:8080.

### Test/Prod

There's no test or prod configs yet. 

## Final Notes

This app is a work in progress!  It'll be changing as I find things to dig into.  See the open issues for future top priorities.

