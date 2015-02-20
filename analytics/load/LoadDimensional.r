# Load dimensional database from staging

library(RMySQL)
library(data.table)
library(dplyr)
library(lubridate)
library(digest)

dimensionalConn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_analytics_demo", username="root")
stagingConn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_staging_demo", username="root")
corpusConn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_person_corpus_demo", username="root")

# clear out dimension tables
dbSendQuery(dimensionalConn, "delete from Agency")
dbSendQuery(dimensionalConn, "delete from County")
dbSendQuery(dimensionalConn, "delete from Date")
dbSendQuery(dimensionalConn, "delete from Disposition")
dbSendQuery(dimensionalConn, "delete from OffenseType")
dbSendQuery(dimensionalConn, "delete from PersonAge")
dbSendQuery(dimensionalConn, "delete from PersonRace")
dbSendQuery(dimensionalConn, "delete from PersonSex")
dbSendQuery(dimensionalConn, "delete from PretrialService")
dbSendQuery(dimensionalConn, "delete from RiskScore")
dbSendQuery(dimensionalConn, "delete from Time")
dbSendQuery(dimensionalConn, "delete from YesNo")
dbSendQuery(dimensionalConn, "delete from AssessedNeed")
dbSendQuery(dimensionalConn, "delete from IncidentType")

# clear out fact tables
dbSendQuery(dimensionalConn, "delete from Incident")
dbSendQuery(dimensionalConn, "delete from ArrestCharge")

copyCodeTable <- function(tableName) {
  dbWriteTable(conn = dimensionalConn, name = tableName, value = dbReadTable(conn = stagingConn, name = tableName),
              , append=TRUE, row.names=FALSE)
}

copyCodeTable("Agency")
copyCodeTable("County")
copyCodeTable("Disposition")
copyCodeTable("OffenseType")
copyCodeTable("PersonRace")
copyCodeTable("PersonSex")
copyCodeTable("RiskScore")
copyCodeTable("AssessedNeed")
copyCodeTable("IncidentType")

PretrialService <- dbReadTable(conn = stagingConn, name="PretrialService")
IsParticipant <- factor(ifelse(PretrialService$PretrialServiceDescription=="unknown", "unknown", "yes"), levels=c("yes","no","unknown"))
PretrialService <- cbind(PretrialService, IsParticipant)
PretrialServiceNoneID <- nrow(PretrialService)+1
PretrialService <- rbind(PretrialService, c(PretrialServiceNoneID, "none", "no"))
PretrialService <- data.table(PretrialServiceID=serviceID, PretrialServiceDescription=serviceDescription, IsParticipant=IsParticipant)
dbWriteTable(conn, "PretrialService", PretrialService, append=TRUE, row.names=FALSE)


yesno <- c("Yes","No")
YesNo <- data.table(YesNoID=1:length(yesno), YesNoDescription=yesno)
dbWriteTable(dimensionalConn, "YesNo", YesNo, append=TRUE, row.names=FALSE)

# note: you can change top age, but it must be divisible by age range step
topAge <- 110
ageRangeStep <- 5
ages <- 0:(topAge+1)
ageCount <- length(ages)
ageStrings <- as.character(ages)
ageStrings[ageCount] <- "Unknown"
ageStrings[ageCount-1] <- paste(ageStrings[ageCount-1],"+",sep="")
ageRanges <- rep("", ageCount)
ageRangesSort <- rep("", ageCount)
currentBottom <- 0
for (a in ages)
{
  currentTop <- currentBottom+(ageRangeStep-1)
  currentTopS <- as.character(currentTop)
  if (currentTop == topAge) currentTopS <- paste(currentTopS,"+",sep="")
  ageRanges[a+1] <- paste(currentBottom, "-", currentTopS, sep="")
  ageRangesSort[a+1] <- paste(formatC(currentBottom, width=3, digits=3, flag="0"), formatC(currentTop, width=3, digits=3, flag="0"), sep = "")
  if ((a+1) %% 5 == 0) currentBottom <- currentBottom + ageRangeStep
}
ageRanges[ageCount] <- ageStrings[ageCount]
ageRanges[ageCount-1] <- ageStrings[ageCount-1]
ageRangesSort[ageCount] <- ageStrings[ageCount]
ageRangesSort[ageCount-1] <- ageStrings[ageCount-1]

PersonAge <- data.table(PersonAgeID=(ages+1), AgeInYears=ageStrings, AgeRange5=ageRanges, AgeRange5Sort=ageRangesSort)
dbWriteTable(dimensionalConn, "PersonAge", PersonAge, append=TRUE, row.names=FALSE)

dates <- seq(from=as.Date("2012-01-01"), to=as.Date("2032-12-31"), by="day")
dateID <- format(dates, "%Y%m%d")
Date <- data.table(DateID=dateID, CalendarDate=dates, DateMMDDYYYY=format(dates, "%m/%d/%Y"), Year=year(dates), YearLabel=as.character(year(dates)), Month=month(dates), MonthName=months(dates), Day=mday(dates), DayOfWeek=weekdays(dates))
dbWriteTable(dimensionalConn, "Date", Date, append=TRUE, row.names=FALSE)

makeTimeID <- function(hours, minutes, seconds) as.integer(paste(formatC(hours, digits=2, width=2, flag="0"), formatC(minutes, digits=2, width=2, flag="0"), formatC(seconds, digits=2, width=2, flag="0"), sep=""))

hours <- rep(0:23, each=3600)
minutes <- rep(rep(0:59, each=60), 24)
seconds <- rep(0:59, 60*24)
timeID <- makeTimeID(hours, minutes, seconds)

Time <- data.table(TimeID=timeID, Hour=hours, Minute=minutes, Second=seconds)
dbWriteTable(dimensionalConn, "Time", Time, append=TRUE, row.names=FALSE)

lookupTimeID <- function(t) {
  hour <- hour(t)
  minute <- minute(t)
  second <- second(t)
  return(filter(Time, Hour==hour, Minute==minute, Second==second)$TimeID)
}

lookupDateID <- function(d) {
  year <- year(d)
  month <- month(d)
  day <- day(d)
  return(filter(Date, Year==year, Month==month, Day==day)$DateID)
}

# load person corpus and assign personal IDs
# we may want to do this in a separate script
# this will be refined per the algorithm that CRG develops
# the rule for now is, records are the same person if the biometric ids are equal OR
# first, last, and middle are equal

# also, of course, we need to modify this to check in the existing corpus first before just assigning
# new hashed IDs

stagingPerson <- dbReadTable(stagingConn, "Person")
biometricPersonIds <- unique(filter(stagingPerson, !is.na(PersonBiometricIdentifier))$PersonBiometricIdentifier)
biometricPersonIdCount <- length(biometricPersonIds)
biometricPerson <- data.table(PersonIdentifierID=1:biometricPersonIdCount,
  PersonIdentifier = unlist(lapply(biometricPersonIds, digest, algo = "sha256")), t_value=biometricPersonIds)

nonbiometricPerson <- filter(stagingPerson, is.na(PersonBiometricIdentifier))
nonbiometricPerson <- mutate(nonbiometricPerson, PersonIdentifier = unlist(lapply(paste(PersonLastName, PersonFirstName, PersonMiddleName, sep="_"), digest, algo = "sha256")))
nonbiometricPerson <- distinct(select(nonbiometricPerson, PersonLastName, PersonFirstName, PersonMiddleName, PersonIdentifier))                                 
nonbiometricPerson <- mutate(nonbiometricPerson, PersonIdentifierID=(biometricPersonIdCount+1):(biometricPersonIdCount+nrow(nonbiometricPerson)))

hashedPerson <- rbind(select(biometricPerson, PersonIdentifierID, PersonIdentifier),
                      select(nonbiometricPerson, PersonIdentifierID, PersonIdentifier))
PersonIdentifier <- data.table(PersonIdentifierID=hashedPerson$PersonIdentifierID, PersonIdentifier=hashedPerson$PersonIdentifier)
dbWriteTable(corpusConn, "PersonIdentifier", PersonIdentifier, append=TRUE, row.names=FALSE)

matchCriteriaId <- 1:4
matchCriteriaName <- c("lastName", "firstName", "middleName", "biometricIdentifier")
MatchCriteria <- data.table(MatchCriteriaID=matchCriteriaId, MatchCriteriaName=matchCriteriaName)
dbWriteTable(corpusConn, "MatchCriteria", MatchCriteria, append=TRUE, row.names=FALSE)

biometricAssoc <- data.table(PersonIdentifierMatchCriteriaID=1:nrow(biometricPerson),
                             PersonIdentifierID=biometricPerson$PersonIdentifierID,
                             MatchCriteriaID=rep(4, nrow(biometricPerson)),
                             MatchCriteriaValue=biometricPerson$PersonIdentifier)

nonbiometricAssoc <- data.table(mutate(rbind(
  select(mutate(nonbiometricPerson, MatchCriteriaID=1, MatchCriteriaValue=PersonLastName), PersonIdentifierID, MatchCriteriaID, MatchCriteriaValue),
  select(mutate(nonbiometricPerson, MatchCriteriaID=2, MatchCriteriaValue=PersonFirstName), PersonIdentifierID, MatchCriteriaID, MatchCriteriaValue),
  select(mutate(nonbiometricPerson, MatchCriteriaID=3, MatchCriteriaValue=PersonMiddleName), PersonIdentifierID, MatchCriteriaID, MatchCriteriaValue)),
  PersonIdentifierMatchCriteriaID=(nrow(biometricAssoc)+1):(nrow(biometricAssoc)+3*nrow(nonbiometricPerson))))
nonbiometricAssoc <- select(nonbiometricAssoc, PersonIdentifierMatchCriteriaID, PersonIdentifierID, MatchCriteriaID, MatchCriteriaValue)

identifierAssoc <- rbind(biometricAssoc, nonbiometricAssoc)

dbWriteTable(corpusConn, "PersonIdentifierMatchCriteria", identifierAssoc, append=TRUE, row.names=FALSE)

# (end loading of person corpus)

# todo: when loading ArrestCharge, be sure to use PretrialServiceNoneID to represent arrestees that were not assigned
# to pretrial services.  There may be similar issues with other code tables.

# stagingIncident <- dbReadTable(stagingConn, "Incident")
# dimensionalIncident <- data.table(IncidentID=stagingIncident$IncidentID,
#                                   DateID=unlist(lapply(stagingIncident$IncidentDate, lookupDateID)),
#                                   TimeID=unlist(lapply(stagingIncident$IncidentDate, lookupTimeID)),
#                                   ReportingAgencyID=stagingIncident$ReportingAgencyID,
#                                   IncidentTypeID=stagingIncident$IncidentTypeID)
# dbWriteTable(dimensionalConn, "Incident", dimensionalIncident, append=TRUE, row.names=FALSE)

dbDisconnect(stagingConn)
dbDisconnect(corpusConn)
dbDisconnect(dimensionalConn)
