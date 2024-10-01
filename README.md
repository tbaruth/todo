# Introduction

This repo is just a test repo. I'm just having some fun, learning a few things, and building myself some quick reference material.

Expect this repo to be full of contrived stuff.  It's purely for play.  

## Setup

### Development

Development requires Keycloak for auth purposes (JWT tokens, etc).

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

6. Modify the Dockerfile-keycloak file with the new realm file in the project (or simply replace todo-keycloak-realm.json with your own file).