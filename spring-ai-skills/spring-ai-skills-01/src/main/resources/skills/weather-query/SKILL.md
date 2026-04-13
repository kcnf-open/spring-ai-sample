---
name: weather-query
description: Use this skill when you need to query the current weather or weather forecast for a specific city.
---

# Weather Query Skill

## Instructions
1. Call the provided Python script to fetch weather data.
2. The script requires one parameter: `city_name`.
3. Parse the JSON output and present the weather information in a clear, natural language summary to the user.

## Resources
- **Scripts**: `scripts/query_weather.py` - Fetches weather data from a public API.
- **References**: `references/api_docs.txt` - Contains API endpoint documentation.

## Example Usage
User asks: "What's the weather like in Beijing?"
Agent should: Execute `python query_weather.py --city Beijing` and summarize the result.