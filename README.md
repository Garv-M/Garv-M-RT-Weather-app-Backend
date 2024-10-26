# Real-Time Data Processing System for Weather Monitoring with Rollups and Aggregates
The system will continuously retrieve weather data from the OpenWeatherMap API.<br>

## Processing and Analysis:
- Thesystem should continuously call the OpenWeatherMap API at a configurable interval(e.g., every 5 minutes) to retrieve real-time weather data for the metros in India. (Delhi, Mumbai, Chennai, Bangalore, Kolkata, Hyderabad)
- For each received weather update:
- Convert temperature values from Kelvin to Celsius.
<br>

# Video Demo
[https://youtu.be/Y23-LTRAWsM?si=ABVrp2SRyRXrzZvo](https://www.youtube.com/watch?v=2JteKSRwp_k)

## Getting Started

### Prerequisites
- **Java SDK 17 or above** and **Maven** installed on your system.
- **MongoDB** installed or a connection URI from MongoDB Atlas.

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Garv-M/AST-RuleEngine-Backend.git
   cd AST-RuleEngine-Backend

2. **Connect your Mongo DB cluster**
   ```bash
   src/main/resources/application.properties
3. **Run the Backend Server**
   ```bash
   mvn spring-boot:run

## End Points
- `GET /all` - Get weather data for all cities</br>
- `GET /{city}` - Get weather data for a specific city</br>
- `POST /alert-config` - Save the alert configs from the user</br>
- `POST /triggered-alerts`- To get the total alert configs</br>
- `GET /daily-summaries`- Get daily summaries</br>

## Frontend
https://github.com/Garv-M/RT-Weather-App
