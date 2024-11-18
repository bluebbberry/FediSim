# FediverseSim

Fediverse simulation software.
Provides an API to simulate a customizable fediverse model for the next few years.

API-requests can be send to `/simulate``-POST with the following JSON-body:

```json
{
    "servers": [
        {
            "name": "Lemmy",
            "usersPerMonth": 3000
        },
        {
          "name": "Mastodon",
          "usersPerMonth": 40000
        },
        {
          "name": "Bluesky",
          "usersPerMonth": 80000
        }
    ],
    "year": 2024
}
```

Based on rational choice theory with the goal of the Fediverse helping all of humanity.
