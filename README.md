# Spring Boot Social Interaction System

This project is a backend system built using Spring Boot, PostgreSQL, and Redis.  
It simulates a social platform where users and bots interact through posts and comments, with controlled logic using Redis.

## Features

- Create Posts API
- Create Comments API
- Comment depth limit (max 20 levels)
- Bot reply limit (max 100 per post using Redis)
- Cooldown system to prevent repeated bot interactions
- Virality score tracking using Redis
- Notification batching using Redis and scheduled tasks

## Tech Stack

- Java 17
- Spring Boot
- PostgreSQL
- Redis
- Docker

## How to Run

1. Start services using Docker:
   docker compose up -d
2. Use Postman or any API client to test endpoints

## Sample APIs

### Create Post 
   -  POST /api/posts
## Notes

- Redis is used for handling concurrency, rate limiting, and temporary data.
- Bot detection is simulated using negative author IDs.