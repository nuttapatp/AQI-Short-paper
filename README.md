# AQI Data Pipeline

Hourly data pipeline that fetches air quality data from OpenWeatherMap and stores it in Google BigQuery — feeds historical and forecast AQI to the API service for Claude AI trend analysis.

**Live service:** [aqi-short-paper.onrender.com](https://aqi-short-paper.onrender.com)

---

## Architecture

```
OpenWeatherMap API
        │
        │  current pollution + 5-day forecast
        ▼
┌──────────────────────────────────────┐
│        Spring Boot (8080)             │
│                                       │
│  ┌────────────────────────────────┐  │
│  │   PollutionDataService          │  │
│  │   @Scheduled cron: 0 0 * * * * │  │
│  │   (runs every hour)             │  │
│  └──────────────┬─────────────────┘  │
│                 │                     │
│  ┌──────────────▼─────────────────┐  │
│  │   OpenWeatherMapClient          │  │
│  │   - fetchCurrentAQI()           │  │
│  │   - fetchForecastAQI()          │  │
│  └──────────────┬─────────────────┘  │
└─────────────────┼─────────────────────┘
                  │
                  ▼
        Google BigQuery
        project: air-quality-api-491405
        dataset: currentapi
        ├── currentaqi   (city, aqi, lat, lon, timestamp)
        └── forecastaqi  (city, aqi, lat, lon, timestamp)
                  │
                  ▼
        API-Short-paper reads BigQuery
        → Claude AI analyzes trend
        → LINE bot replies with forecast
```

---

## Tech Stack

| | |
|---|---|
| Backend | Spring Boot 2.7.18, Java 17 |
| Data Source | OpenWeatherMap Air Pollution API |
| Storage | Google BigQuery |
| Scheduler | Spring `@Scheduled` (hourly cron) |
| Deployment | Render |

---

## Cities Covered

| City | Coordinates |
|---|---|
| Bangkok | 13.7563, 100.5018 |
| Chiang Mai | 18.7883, 98.9853 |

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/pollution/current/all` | Trigger manual fetch for all cities |
| GET | `/api/v1/pollution/forecast/all` | Trigger manual forecast fetch for all cities |
| GET | `/api/v1/pollution/current?city=Bangkok` | Fetch current AQI for specific city |
| GET | `/api/v1/pollution/forecast?city=Bangkok` | Fetch forecast for specific city |

---

## Environment Variables

| Variable | Description |
|---|---|
| `OPENWEATHER_API_KEY` | OpenWeatherMap API key |
| `BIGQUERY_CREDENTIALS` | Path to BigQuery service account JSON (e.g. `/etc/secrets/bigquery.json`) |

---

## Local Development

```bash
# 1. Create .env file
cp .env.example .env
# Fill in OPENWEATHER_API_KEY and place BigQuery credentials JSON in src/main/resources/

# 2. Run
mvn spring-boot:run
```

---

## Related Repos

- [API-Short-paper](../API-Short-paper) — LINE bot + REST API (reads from BigQuery)
- [aqi-map-short-paper](../aqi-map-short-paper) — Live map dashboard
