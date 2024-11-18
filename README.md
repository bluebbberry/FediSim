# FediverseSim

Fediverse simulation software.
Provides an API to simulate a customizable fediverse model for the next few years.

API-requests can be send to `/simulate``-POST with the following JSON-body:

````json
{
    "servers": [
        {
            "name": "lemmy",
            "usersPerMonth": 3000
        }
    ],
    "year": 1000
}
````
