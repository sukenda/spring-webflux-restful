// docker-compose -f docker-compose/mongodb-compose.yaml up -d
// docker exec -it mongodb /bin/bash
// mongo -u root -p Pas@123 --authenticationDatabase admin
// mongo -u spring-webflux-user -p spring-webflux-pass --authenticationDatabase spring-webflux-restful
// use spring-webflux-restful
db.createUser({
    user: 'spring-webflux-user',
    pwd: 'spring-webflux-pass',
    roles: [
        {
            role: 'readWrite',
            db: 'spring-webflux-restful'
        }
    ]
})


