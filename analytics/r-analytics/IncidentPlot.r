STATE = 'ME'

library(rgdal)
library(ggplot2)
library(ggmap)
library(dplyr)
library(RMySQL)
library(ggmap)

state_shp <- readOGR("/opt/data//Shapefiles/tl_2014_us_state", "tl_2014_us_state")
county_shp = readOGR("/opt/data/Shapefiles/tl_2014_us_county/", "tl_2014_us_county")

state_df <- state_shp@data
stateFips <- as.character(filter(state_df, STUSPS==STATE)$STATEFP)

county_shp <- subset(county_shp, STATEFP == stateFips)
county_shp_df <- fortify(county_shp)

state_shp <- subset(state_shp, STATEFP == stateFips)
state_shp_df <- fortify(state_shp)

conn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_analytics_demo", username="root")
Incident <- dbFetch(dbSendQuery(conn, "select IncidentLocationLongitude, IncidentLocationLatitude, IncidentTypeDescription
                                from Incident, IncidentType where Incident.IncidentTypeID=IncidentType.IncidentTypeID"))


map <- ggplot() +
  geom_path(data = county_shp_df, aes(x = long, y = lat, group = group), color = 'black', fill = 'white', size = .3) +
  geom_path(data = state_shp_df, aes(x = long, y = lat, group = group), color = 'black', fill = 'white', size = .4) +
  geom_point(data=Incident, aes(x = IncidentLocationLongitude, y=IncidentLocationLatitude, colour=IncidentTypeDescription)) +
  coord_map(projection="mercator")

map

city <- get_map("Augusta, Maine", zoom=12, source = "google")
map <- ggmap(city) + 
  geom_point(data=Incident, aes(x = IncidentLocationLongitude, y=IncidentLocationLatitude, colour=IncidentTypeDescription), size=4)
map

dbDisconnect(conn)