# Loads the dimensional database with dummy/demo data

STATE <- "ME"

demoArresteeCount = 500
demoIncidentCount = 750

DATE_ID_FORMAT <- "%Y%m%d"

library(RMySQL)
library(data.table)
library(dplyr)
library(rgdal)
library(sp)

state_shp <- readOGR("/Users/scott/Documents/R-expt/Shapefiles/tl_2014_us_state", "tl_2014_us_state")
county_shp = readOGR("/Users/scott/Documents/R-expt/Shapefiles/tl_2014_us_county/", "tl_2014_us_county")
countyData <- read.csv("CountyData.csv")

state_df <- state_shp@data
stateFips <- as.character(filter(state_df, STUSPS==STATE)$STATEFP)

county_df <- filter(county_shp@data, STATEFP==stateFips)

getCountyProbs <- function() {
  counties <- filter(countyData, fips %in% county_df$GEOID)
  statePop <- sum(counties$PST045213)
  #countyProb <- rep(.05, nrow(county_df) + 1)
  # todo: change this to scale probs based on census population
  #countyProb[9] <- .3 # chittenden
  countyProb <- (.9*counties$PST045213)/statePop
  c(countyProb, .1)
}

#conn <- dbConnect(MySQL(), host="dw", dbname="ojbc_analytics_demo", username="root")
conn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_analytics_demo", username="root")

# clear out fact tables
dbSendQuery(conn, "delete from Incident")
dbSendQuery(conn, "delete from Arrest")
dbSendQuery(conn, "delete from Charge")
dbSendQuery(conn, "delete from PretrialServiceParticipation")

# clear out dimension tables
dbSendQuery(conn, "delete from Agency")
dbSendQuery(conn, "delete from County")
dbSendQuery(conn, "delete from Date")
dbSendQuery(conn, "delete from Disposition")
dbSendQuery(conn, "delete from OffenseType")
dbSendQuery(conn, "delete from PersonAge")
dbSendQuery(conn, "delete from PersonRace")
dbSendQuery(conn, "delete from PersonSex")
dbSendQuery(conn, "delete from PretrialService")
dbSendQuery(conn, "delete from RiskScore")
dbSendQuery(conn, "delete from Time")
dbSendQuery(conn, "delete from YesNo")
dbSendQuery(conn, "delete from AssessedNeed")
dbSendQuery(conn, "delete from IncidentType")

sexes <- c("Male","Female","Unknown")
PersonSex <- data.table(PersonSexID=1:length(sexes), PersonSexDescription=sexes)
dbWriteTable(conn, "PersonSex", PersonSex, append=TRUE, row.names=FALSE)

races <- c("BLACK","ASIAN","WHITE","UNKNOWN","AMERICAN INDIAN")
PersonRace <- data.table(PersonRaceID=1:length(races), PersonRaceDescription=races)
dbWriteTable(conn, "PersonRace", PersonRace, append=TRUE, row.names=FALSE)

yesno <- c("Yes","No")
YesNo <- data.table(YesNoID=1:length(yesno), YesNoDescription=yesno)
dbWriteTable(conn, "YesNo", YesNo, append=TRUE, row.names=FALSE)

incidentType <- c("Incident Type A", "Incident Type B", "Unknown")
IncidentType <- data.table(IncidentTypeID=1:length(incidentType), IncidentTypeDescription=incidentType)
dbWriteTable(conn, "IncidentType", IncidentType, append=TRUE, row.names=FALSE)

need <- c("Need 1","Need 2", "Need 3", "None", "Unknown")
AssessedNeed <- data.table(AssessedNeedID=1:length(need), AssessedNeedDescription=need)
dbWriteTable(conn, "AssessedNeed", AssessedNeed, append=TRUE, row.names=FALSE)

countyID <- c(as.character(county_df$GEOID), "99999")
countyName <- c(as.character(county_df$NAME), "Unkown")
County <- data.table(CountyID=countyID, CountyName=countyName)
dbWriteTable(conn, "County", County, append=TRUE, row.names=FALSE)

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
dbWriteTable(conn, "PersonAge", PersonAge, append=TRUE, row.names=FALSE)

offenseID <- c(2204, 0102, 1103, 4904, 4005, 2001, 3916, 6409, 2510, 4199, 6404, 2502,
               0912, 1212, 2703, 3911, 3540, 7399, 5704, 2199, 3617, 1601, 5499, 4804,
               5109, 7299, 2311, 3803, 5503, 3606, 3920, 5002, 5217, 3543, 1106, 1304,
               1020, 3564, 6204, 5510, 3581, 5399, 4809, 4999, 5802, 2313, 4007, 3502,
               3550, 5207, 1007, 1111, 2305, 1206, 5521, 4104, 5102, 5705, 1312, 5402,
               3899, 3570, 1702, 1021, 1122, 5213, 7199, 4810, 5304, 5009, 3618, 5202,
               1117, 2607, 0903, 1307, 5307, 5309, 4902, 2505, 5699, 3610, 6099, 5104,
               0106, 3601, 1009, 1303, 2203, 6203, 6101, 1310, 5706, 2102, 3905, 3503,
               5214, 6407, 2899, 2315, 1112, 2407, 0901, 3706, 4004, 1204, 1404, 4006,
               3583, 0199, 3804, 5014, 1110, 1315, 5212, 5299, 2904, 0913, 3611, 3805,
               5899, 3702, 2504, 0105, 4899, 5999, 5303, 2704, 3999, 2401, 0911, 2201,
               5211, 4802, 5004, 0906, 1011, 1201, 4103, 3560, 1005, 2803, 3513, 1109,
               2314, 6406, 5015, 3521, 2207, 2503, 2805, 1104, 6408, 1114, 5522, 6201,
               5401, 2409, 0301, 5305, 5703, 1004, 2509, 3561, 1211, 1208, 8101, 3532,
               5107, 1305, 1314, 5006, 2007, 5111, 2608, 5113, 8001, 5108, 0902, 2408,
               5502, 2307, 2603, 2005, 2601, 6300, 3523, 5311, 2609, 5203, 0908, 2906,
               2402, 3901, 4009, 3907, 2312, 3707, 3915, 6199, 3616, 4901, 2901, 3607,
               1123, 6402, 1402, 3622, 8003, 1006, 4101, 5016, 2104, 5707, 1118, 0904,
               1316, 1210, 4813, 5204, 1119, 7099, 2299, 5010, 2801, 1116, 3504, 5799,
               3582, 2202, 4002, 0999, 5216, 0905, 3704, 2308, 5210, 6103, 5801, 5112,
               2508, 2606, 6205, 3902, 3615, 2804, 0201, 1099, 5008, 1311, 3699, 0302,
               2701, 2599, 1199, 2699, 5199, 0299, 2103, 3531, 3533, 4903, 2105, 2411,
               3614, 1198, 3608, 3910, 4008, 2410, 2206, 1313, 2705, 5599, 2099, 4805,
               3505, 1306, 2499, 5501, 0104, 1115, 5404, 1207, 3501, 3603, 2999, 2602,
               3808, 5012, 2304, 4001, 1602, 2604, 6401, 3799, 5511, 3908, 1701, 3562,
               5301, 1120, 2309, 3918, 2399, 2009, 5209, 2003, 3801, 3809, 3605, 2310,
               1001, 0399, 3802, 3807, 2406, 3542, 1010, 2004, 1105, 2205, 2405, 6410,
               2903, 4803, 3510, 0907, 2589, 3580, 6499, 5110, 5106, 5512, 2905, 8004,
               1299, 5708, 1399, 1203, 5001, 2511, 3703, 0910, 4806, 3904, 1309, 5011,
               5103, 4807, 4811, 3621, 2008, 5702, 5208, 6202, 1205, 5005, 3700, 2301,
               5803, 3520, 1308, 1202, 2404, 3571, 5403, 3912, 1108, 0103, 2507, 3913,
               3604, 5206, 4099, 2302, 5007, 1209, 3701, 2802, 2403, 3530, 1301, 4801,
               4808, 5205, 5013, 3512, 2902, 2101, 5101, 6102, 1302, 2702, 1102, 5520,
               2002, 5306, 3705, 0101, 8005, 3609, 3921, 3806, 6403, 2501, 3573, 3914,
               2506, 3511, 2799, 3811, 3919, 8100, 2605, 0909, 1403, 1008, 5099, 5105,
               3619, 1121, 3613, 4102, 2306, 3563, 1002, 3810, 3522, 4812, 1499, 1003,
               3812, 1401, 8102, 5201, 3909, 5003, 6405, 5215, 5701, 3599, 6299, 2006,
               3620, 3541, 1107, 1101, 0303, 2316, 1113, 3572, 2303, 4003, 3612, 3602,
               5302, 5310, 9998, 9999)
offenseName <- c("BURGL - NO FORCED ENTRY-RESID", "TREASON MISPRISION", "RAPE - STRONGARM", "HARBORING ESCAPE/FUGITIVE", "FREQUENT HOUSE ILL FAME", "ARSON - BUSINESS-ENDANGERED LIFE", "LOTTERY - RUNNER", "ENTICEMENT OF MINOR FOR PROSTITUTION", "TRANSPORT COUNTERFEITED - SEE MIS", "LIQUOR", "SEXUAL EXPLOITATION OF MINOR-EXHIBITION OF MINOR", "FORGERY OF", "HOMICIDE - WILLFUL KILL-WEAPON", "CARJACKING - ARMED", "EMBEZZLE - BANKING-TYPE INST", "GAMBLING DEVICE", "SYNTH NARCOTIC - SELL", "PUBLIC ORDER CRIMES SEE MIS", "EAVESDROPPING", "EXTORTION", "SEXUALLY VIOLATE HUMAN REMAINS/NECROPHILIA", "THREAT - FEDERAL PROTECTEES", "TRAFFIC OFFENSE", "EVIDENCE - DESTROYING", "GRATUITY", "MORALS - DECENCY CRIMES SEE MIS", "LARC - FROM BANKING-TYPE INST", "CRUELTY TOWARD WIFE", "DRUGS - HEALTH OR SAFETY", "BESTIALITY", "TRANSMIT WAGER INFORMATION", "BAIL - PERSONAL RECOG", "WEAPON TRAFFICKING", "SYNTH NARCOTIC",
                 "SEX ASSLT - SODOMY-GIRL-GUN", "AGGRAV ASSLT - NONFAMILY-GUN", "FALSE IMPRISONMENT-MINOR-NONPARENTAL", "MARIJUANA", "CONSERVATION - LICENSE-STAMP", "FOOD - ADULTERATED", "BARBITURATE - SELL", "PUBLIC PEACE", "UNAUTH COMMUNICATION WITH PRISONER", "FLIGHT-ESCAPE", "SMUGGLE CONTRABAND INTO PRISON", "OBSTRUCT CORRESPONDENCE", "PROCURE FOR PROSTITUTE WHO IS A MINOR", "HALLUCINOGEN - DISTRIB", "NARCOTIC EQUIP - POSSESS", "INCENDIARY DEVICE - POSSESS", "KIDNAP HOSTAGE FOR ESCAPE", "SEX ASSLT - SODOMY-WOMAN-WEAPON", "LARC - FROM AUTO", "ROBBERY - STREET-STGARM", "COSMETICS - MISBRANDED", "LIQUOR - POSSESS", "BRIBE - OFFERING", "EAVESDROP EQUIP", "AGGRAV ASSLT - POL OFF-STGARM", "TRANSP DANGEROUS MATERIAL", "FAMILY OFFENSE", "AMPHETAMINE - MFR", "FEDERAL - MATERIAL WITNESS", "FALSE IMPRISONMENT-MINOR-PARENTAL", "RAPE-DRUG-INDUCED", "FIRING WEAPON", "PROPERTY CRIMES SEE MIS",
                 "ILLEGAL ARREST", "RIOT - INTERFERE FIREMAN", "CONTEMPT OF CONGRESS", "MOLESTATION OF MINOR", "CARRYING CONCEALED WEAPON", "SEX ASSLT - CARNAL ABUSE", "FRAUD - FALSE STATEMENT", "HOMICIDE - WILLFUL KILL-NONFAMILY-GUN", "AGGRAV ASSLT - PUB OFF-GUN", "ASSEMBLY - UNLAWFUL", "HARASSING COMMUNICATION", "FLIGHT TO AVOID SEE MIS", "PASS COUNTERFEITED", "CIVIL RIGHTS", "HOMOSEXUAL ACT WITH MAN", "ANTITRUST", "BRIBE", "SELECTIVE SERVICE", "SEX OFFENSE - AGAINST CHILD-FONDLING", "KIDNAP - HIJACK AIRCRAFT", "AGGRAV ASSLT - FAMILY-STGARM", "BURGL - FORCED ENTRY-NONRESID", "CONSERVATION - BIRDS", "INCOME TAX", "AGGRAV ASSLT - POL OFF-GUN", "OPENING SEALED COMMUNICATION", "EXTORT - THREAT DAMAGE PROP", "DICE GAME - OPERATING", "HALLUCINOGEN - SELL", "SELLING WEAPON", "SEXUAL EXPLOITATION OF MINOR-PROSTITUTION", "STOLEN PROPERTY", "LARC ON US GOVT RESERV", "SEX ASSLT - SODOMY-BOY-STGARM", "STRIP STOLEN VEH",
                 "HOMICIDE - WILLFUL KILL-FAMILY-GUN", "OBSCENE MATERIAL - TRANSPORT", "PROSTITUTION", "ROBBERY - STREET-GUN", "ABORTIFACIENT", "TRNSP FEMALE INTERSTATE FOR IMMORAL PURP", "BARBITURATE", "SOVEREIGNTY", "BIGAMY", "MANDATORY RELEASE VIOLATION", "SEX ASSLT - SODOMY-GIRL-WEAPON", "AGGRAV ASSLT - WEAPON", "POSSESSION OF WEAPON", "WEAPON OFFENSE", "DAMAGE PROP - BUSINESS-WITH EXPLOSIVE", "HOMICIDE - JOHN OR JANE DOE - NO WARRANT", "PEEPING TOM", "CONTRIB DELINQ MINOR", "SMUGGLING", "OBSCENE MATERIAL - SELL", "PASS FORGED", "SEDITION", "OBSTRUCT POLICE", "ELECTION LAWS", "RIOT - ENGAGING IN", "EMBEZZLE - PUBLIC PROP", "GAMBLING", "THEFT AND SALE VEH", "HOMICIDE - WILLFUL KILL-GUN", "BURGL - SAFE-VAULT", "EXPLOSIVES - POSSESSING", "OBSTRUCT CRIMINAL INVEST", "PERJURY - SUBORNATION OF", "HOMICIDE - WILLFUL KILL-PUB OFF-WEAPON", "KIDNAP MINOR-NONPARENTAL", "ROBBERY - BUSINESS-GUN", "LIQUOR - TRANSPORT",
                 "MARIJUANA - SELL", "KIDNAP MINOR", "RECEIVE STOLEN PROP", "HEROIN", "SEX ASSLT - SODOMY-MAN-WEAPON", "THEFT OF US GOVT PROP", "SEXUAL EXPLOITATION OF MINOR-VIA TELECOMMUNICATIONS", "FAILURE TO APPEAR - SEE MIS", "OPIUM OR DERIV - SMUGGL", "BURGL - BANKING-TYPE INST", "COUNTERFEITING OF", "CONCEAL STOLEN PROP", "SEX ASSLT - SODOMY-BOY-GUN", "ENTICEMENT OF MINOR FOR INDECENT PURPOSES-VIA TELECOMMUNICATIONS", "SEX ASSLT - SODOMY-GIRL-STGARM", "COSMETICS - HEALTH OR SAFETY", "CONSERVATION - ANIMALS", "HIT AND RUN", "INTERSTATE TRANSP STOLEN VEH", "ILLEGAL ENTRY",
                 "RIOT - INTERFERE OFFICER", "DIVULGE MESSAGE CONTENTS", "KIDNAP ADULT TO SEXUALLY ASSLT", "TRANSPORT FORGED - SEE MIS", "MARIJUANA - SMUGGL", "ROBBERY - BANKING-TYPE INST", "ROBBERY - RESID-WEAPON", "ABSCOND WHILE ON PAROLE", "COCAINE - POSSESS", "GRATUITY - OFFERING", "AGGRAV ASSLT - NONFAMILY-WEAPON", "AGGRAV ASSLT - GUN", "OBSTRUCTING JUSTICE", "BURNING OF-SEE MIS", "KICKBACK - OFFERING", "FRAUD - BY WIRE", "KICKBACK", "SUBJECT IS A THREAT TO A U.S. SECRET SERVICE PROTECTEE", "GRATUITY - RECEIVING", "HOMICIDE - WILLFUL KILL-FAMILY-WEAPON", "POSSESS STOLEN VEHICLE", "DRUGS - MISBRANDED", "LARC - FROM COIN MACHINE", "MAIL FRAUD", "ARSON - BUSINESS", "FRAUD - CONFIDENCE GAME", "MONEY LAUNDERING", "OPIUM OR DERIV", "DISORD CONDUCT", "FRAUD AND ABUSE, COMPUTER", "CARRYING PROHIBITED WEAPON",
                 "HOMICIDE - WILLFUL KILL-POL OFF-WEAPON", "DAMAGE PROP - PUBLIC-WITH EXPLOSIVE", "THEFT AND STRIP VEH", "BOOKMAKING", "TRANSPORT INTERSTATE FOR COMMERCIALIZED SEX", "DICE GAME", "LARC - FROM INTERSTATE SHIPMENT", "OBSCENE COMMUNICATION", "LOTTERY - OPERATING", "TAX REVENUE", "LEWD OR LASCIVIOUS ACTS WITH MINOR", "ESCAPE SEE MIS", "DAMAGE PROP - BUSINESS", "INCEST WITH ADULT", "SEXUAL ASSAULT-DRUG-INDUCED", "SEXUAL EXPLOITATION OF MINOR-MATERIAL-PHOTOGRAPH", "ABORTIONAL ACT ON SELF", "TRANSPORT INTERSTATE FOR SEXUAL ACTIVITY", "SUBJECT IS A DEPORTED CRIMINAL/AGGRAVATED FELON", "KIDNAP ADULT", "LIQUOR - MFR", "VIOLATION OF A COURT ORDER", "EXTORT - THREAT ACCUSE PERSON OF CRIME", "TRESPASSING", "RAPE-ELDERLY", "HOMICIDE - WILLFUL KILL-NONFAMILY-WEAPON", "INTIMIDATION (INCLUDES STALKING)", "FORCIBLE PURSE SNATCHING",
                 "FAILURE TO MOVE ON", "EXPLOSIVES - TEACHING USE", "RAPE-DISABLED", "CRIMES AGAINST PERSON SEE MIS", "BURGLARY", "CONTEMPT OF LEGISLATURE", "SALE OF STOLEN PROP", "STAT RAPE - NO FORCE", "HALLUCINOGEN - POSSESS", "INVADE PRIVACY", "BARBITURATE - POSSESS", "BURGL - FORCED ENTRY-RESID", "PROCURE FOR PROSTITUTE (PIMPING)", "HOMICIDE", "THREAT TO BURN", "HOMICIDE - WILLFUL KILL-PUB OFF-GUN", "OBSCENE MATERIAL - POSSESS", "LARC - FROM BLDG", "LICENSING - REGISTRATION WEAPON", "LIQUOR TAX", "SMUGGLE CONTRABAND", "KICKBACK - RECEIVING", "POSSESS TOOLS FOR FORGERY/COUNTERFEITING", "FRAUD - INSUFF FUNDS CHECK", "CONSERVATION - ENVIRONMENT", "CARD GAME - OPERATING", "INDECENT EXPOSURE TO ADULT", "POSSESS STOLEN PROP", "MILITARY DESERTION", "KIDNAPPING", "MISCONDUCT - JUDIC OFFICER", "AGGRAV ASSLT - POL OFF-WEAPON", "SEX OFFENSE", "FALSE CITIZENSHIP", "EMBEZZLE - BUSINESS PROP",
                 "COUNTERFEITING", "SEX ASSAULT", "FRAUD", "BRIBERY", "MILITARY", "EXTORT - THREAT INJURE REPUTATION", "COCAINE - SMUGGL", "COCAINE", "AIDING PRISONER ESCAPE SEE MIS", "EXTORT - THREAT OF INFORMING OF VIO", "UNAUTH USE OF VEH", "INDECENT EXPOSURE TO MINOR", "RAPE", "SEDUCTION OF ADULT", "GAMBLING DEVICE - NOT REGISTERED", "PROCURE FOR PROSTITUTE WHO IS AN ADULT", "AIRCRAFT THEFT", "BURGL TOOLS - POSSESS", "SIMPLE ASSLT", "EMBEZZLE - POSTAL", "HEALTH - SAFETY", "ARSON", "WITNESS - DISSUADING", "HALLUCINOGEN", "AGGRAV ASSLT - NONFAMILY-STGARM", "STOLEN VEHICLE", "DRUGS - ADULTERATED", "SABOTAGE", "SEX ASSLT - SODOMY-WOMAN-STGARM", "DRIVING UNDER INFLUENCE ALCOHOL", "ROBBERY - RESID-GUN", "HALLUCINOGEN - MFR", "HOMOSEXUAL ACT WITH BOY", "DAMAGE PROPERTY", "FRAUD - SWINDLE", "NONSUPPORT OF PARENT", "PROB VIOLATION - SEE MIS", "LARC - PARTS FROM VEH", "KEEPING HOUSE ILL FAME",
                 "THREAT - TERRORISTIC - STATE OFFENSES", "FRAUD - IMPERSON", "SEXUAL EXPLOITATION OF MINOR-MATERIAL-TRANSPORT", "OBSCENITY", "FOOD - MISBRANDED", "GAMBLING DEVICE - POSSESS", "STATE/LOCAL - MATERIAL WITNESS", "MARIJUANA - POSSESS", "ANARCHISM", "SEX ASSAULT-ELDERLY", "LARC - FROM YARDS", "LOTTERY", "LARCENY", "ARSON - PUB-BLDG", "INCENDIARY DEVICE - TEACHING USE", "ARSON - BUSINESS-DEFRAUD INSURER", "NEGLECT FAMILY", "CRUELTY TOWARD ELDERLY", "INDECENT EXPOSURE (TO MINORS AND ADULTS)", "LARC - FROM MAILS", "KIDNAP MINOR FOR RANSOM", "IMMIGRATION", "CRUELTY TOWARD CHILD", "NONPAYMENT OF ALIMONY", "RECEIV STOLEN VEH", "SYNTH NARCOTIC - POSSESS", "KIDNAP MINOR-PARENTAL", "ARSON - RESID-DEFRAUD INSURER", "SEX ASSLT - SODOMY-MAN-GUN", "BURGL - NO FORCED ENTRY-NONRESID", "THEFT VEH BY BAILEE", "ENTICEMENT OF MINOR FOR INDECENT PURPOSES", "DAMAGE PROP - PUBLIC", "MAKING FALSE REPORT", "HEROIN - SELL",
                 "HOMICIDE - WILLFUL KILL-POL OFF-GUN", "FORGERY", "BARBITURATE - MFR", "EXPLOITATION/ENTICEMENT", "KICKBACK - GIVING", "GRATUITY - GIVING", "FOOD - HEALTH OR SAFETY", "DAMAGE PROP - PRIVATE-WITH EXPLOSIVE", "ALIEN UNLAWFULLY PRESENT DUE TO ORDER OF REMOVAL OR EXCLUSION FROM THE USA", "ROBBERY", "WIRETAP - FAILURE TO REPORT", "ASSAULT", "ROBBERY - BUSINESS-STGARM", "BAIL - SECURED BOND", "TRANSPORT TOOLS FOR FORGERY/COUNTERFEITING", "OBSCENE MATERIAL - MAILING", "HOMICIDE - NEGLIG MANSL-WEAPON", "WITNESS - DECEIVING", "CARD GAME", "AGGRAV ASSLT - PUB OFF-STGARM", "PAROLE VIOLATION - SEE MIS", "BRIBE - RECEIVING", "REFUSING TO AID OFFICER", "CROSSING POLICE LINES", "SEX OFFENSE-DISABLED", "ARSON - PUB-BLDG-ENDANGERED LIFE", "DIVULGE EAVESDROP ORDER", "INCENDIARY DEVICE - USING", "CONSERVATION - FISH", "ROBBERY - STREET-WEAPON", "CONTEMPT OF COURT - SEE MIS", "OBSCENE MATERIAL", "POCKETPICKING",
                 "SMUGGLE TO AVOID PAYING DUTY", "OPIUM OR DERIV - SELL", "AGGRAV ASSLT - PUB OFF-WEAPON", "ROBBERY - BUSINESS-WEAPON", "VEHICLE THEFT", "AMPHETAMINE - SELL", "DRIVING UNDER INFLUENCE DRUGS", "GAMBLING GOODS - POSSESS", "SEX ASSLT - SODOMY-BOY-WEAPON", "ESPIONAGE", "POSSESS COUNTERFEITED - SEE MIS", "GAMBLING GOODS - TRANSPORT", "INCEST WITH MINOR", "EXPLOSIVES - USING", "COMMERCIAL SEX", "PURSE SNATCHING - NO FORCE", "OBSTRUCTING COURT ORDER", "ROBBERY - RESID-STGARM", "OBSCENE MATERIAL - MFR", "TRANSPORT INTERSTATE STOLEN PROP", "THEFT AND USE VEH OTHER CRIME", "COCAINE - SELL", "AGGRAV ASSLT - FAMILY-GUN", "RESISTING OFFICER", "COMPOUNDING CRIME", "EXPLOSIVES - TRANSPORTING", "CONDIT RELEASE VIOLATION", "HEROIN - POSSESS", "DAMAGE PROP - PRIVATE", "EXTORT - THREAT INJURE PERSON", "BRIBE - GIVING", "SALES TAX", "AGGRAV ASSLT - FAMILY-WEAPON", "EMBEZZLE - INTERSTATE SHIPMENT", "RAPE WITH WEAPON",
                 "COSMETICS - ADULTERATED", "ARSON - RESID-ENDANGERED LIFE", "RIOT", "OBSCENE MATERIAL - DISTRIB", "TREASON", "SOUGHT FOR VIOLATION OF NATIONAL SECURITY REGISTRATION", "HOMOSEXUAL ACT WITH WOMAN", "ESTABLISH GAMBLING PLACE", "NEGLECT CHILD", "SEXUAL EXPLOITATION OF MINOR-MATERIAL-FILM", "FORGERY OF CHECKS", "AMPHETAMINE", "GAMBLING GOODS", "POSSESS FORGED - SEE MIS", "HEROIN - SMUGGL", "EMBEZZLE", "NEGLECT ELDERLY", "SPORTS TAMPERING", "ESCAPE FROM CUSTODY", "FRAUD - ILLEG USE CREDIT CARDS", "HOMICIDE - NEGLIG MANSL-VEH", "SUBMISSION TO ABORTIONAL ACT", "ABDUCT - NO RANSOM OR ASSLT", "OBSTRUCT SEE MIS", "CONFLICT OF INTEREST", "VOYEURISM", "SEX ASSAULT-DISABLED", "SEX OFFENDER REGISTRATION VIOLATION(SEE MIS)", "LIQUOR - SELL", "LARC - FROM SHIPMENT", "MARIJUANA - PRODUCING", "KIDNAP ADULT FOR RANSOM", "CRUELTY TOWARD DISABLED", "OPIUM OR DERIV - POSSESS", "FAILURE REPORT CRIME", "ABORTION", "KIDNAP MINOR TO SEXUALLY ASSLT",
                 "NEGLECT DISABLED", "ABORTIONAL ACT ON OTHER", "ABSCOND WHILE ON PROBATION", "ALTERING IDENTIFICATION ON WEAPON", "GAMBLING DEVICE - TRANSPORT", "PERJURY", "SEXUAL EXPLOITATION OF MINOR-SEX PERFORMANCE", "THREAT TO BOMB", "DIVULGE EAVESDROP INFO", "DANGEROUS DRUGS", "CONSERVATION", "ARSON - RESID", "SEX OFFENSE-ELDERLY", "SYNTH NARCOTIC - SMUGGL", "SEX ASSLT - SODOMY-WOMAN-GUN", "RAPE - GUN", "SMUGGLING ALIENS", "LARC - POSTAL", "SEX ASSLT - SODOMY-MAN-STGARM", "AMPHETAMINE - POSSESS", "SHOPLIFTING", "COMMERCIAL SEX - HOMOSEXUAL PROSTITUTION", "FAILURE TO REGISTER AS A SEX OFFENDER", "HOMOSEXUAL ACT WITH GIRL", "RIOT - INCITING", "DESECRATING FLAG",
                 "NONE", "UNKNOWN")
offenseDrug <- c("N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "Y", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "Y", "N", "N", "N", "N", "Y", "N", "N",
                 "N", "Y", "N", "N", "Y", "N", "N", "N", "N", "N", "N", "Y",
                 "Y", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "Y", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "Y",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "Y", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "Y", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "Y", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "Y", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N", "N", "N", "N", "N", "N", "Y", "N", "N",
                 "N", "Y", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                 "N", "N", "N", "N")

offenseSeverity <- sample(c("Felony","Misdemeanor"), length(offenseID), replace=TRUE)
OffenseType <- data.table(OffenseTypeID=offenseID, OffenseDescription=offenseName, IsDrugOffense=offenseDrug, OffenseSeverity=offenseSeverity)
dbWriteTable(conn, "OffenseType", OffenseType, append=TRUE, row.names=FALSE)

serviceID <- 1:7
serviceDescription <- paste("service", serviceID)
serviceDescription[6] <- "unknown"
serviceDescription[7] <- "none"
isParticipant <- serviceDescription
isParticipant[isParticipant=="none"] <- "no"
isParticipant[1:5] <- "yes"
PretrialService <- data.table(PretrialServiceID=serviceID, PretrialServiceDescription=serviceDescription, IsParticipant=isParticipant)
dbWriteTable(conn, "PretrialService", PretrialService, append=TRUE, row.names=FALSE)

riskScoreID <- 1:4
riskScoreDescription = c("High","Medium","Low","Unknown")
RiskScore <- data.table(RiskScoreID=riskScoreID, RiskScoreDescription=riskScoreDescription)
dbWriteTable(conn, "RiskScore", RiskScore, append=TRUE, row.names=FALSE)

dispoID <- 1:4
dispoDescription <- c("Conviction","Acquittal","Declined Prosecution","Unknown")
isConviction <- c("Y","N","N","N")
Disposition <- data.table(DispositionID=dispoID, DispositionDescription=dispoDescription, IsConviction=isConviction)
dbWriteTable(conn, "Disposition", Disposition, append=TRUE, row.names=FALSE)

agencyID <- 1:11
agencyName <- paste("Agency", agencyID)
agencyName[11] <- "Unknown"
Agency <- data.table(AgencyID=agencyID, AgencyName=agencyName)
dbWriteTable(conn, "Agency", Agency, append=TRUE, row.names=FALSE)

dates <- seq(from=as.Date("2012-01-01"), to=as.Date("2032-12-31"), by="day")
dateID <- format(dates, DATE_ID_FORMAT)
Date <- data.table(DateID=dateID, CalendarDate=dates, DateMMDDYYYY=format(dates, "%m/%d/%Y"), Year=year(dates), YearLabel=as.character(year(dates)), Month=month(dates), MonthName=months(dates), Day=mday(dates), DayOfWeek=weekdays(dates))
dbWriteTable(conn, "Date", Date, append=TRUE, row.names=FALSE)

makeTimeID <- function(hours, minutes, seconds) as.integer(paste(formatC(hours, digits=2, width=2, flag="0"), formatC(minutes, digits=2, width=2, flag="0"), formatC(seconds, digits=2, width=2, flag="0"), sep=""))

hours <- rep(0:23, each=3600)
minutes <- rep(rep(0:59, each=60), 24)
seconds <- rep(0:59, 60*24)
timeID <- makeTimeID(hours, minutes, seconds)

Time <- data.table(TimeID=timeID, Hour=hours, Minute=minutes, Second=seconds)
dbWriteTable(conn, "Time", Time, append=TRUE, row.names=FALSE)

getRandomCoordsInCounty <- function(countyId) {
  if (countyId != "99999") {
    l_county_shp <- subset(county_shp, GEOID == countyId)
    df <- as.data.frame(coordinates(spsample(x=l_county_shp, n = 1, type = "regular", iter=50)))
    df$countyId <- countyId
    return(select(mutate(df[1,], lat=x2, long=x1, CountyId=countyId), CountyId, lat, long))
  }
  data.frame(CountyId=countyId, lat=NA, long=NA)
}

getRandomCoordsInCounties <- function(countyIds) {
  bind_rows(Map(getRandomCoordsInCounty, countyIds))
}

#
# Arrest
# 

arrestID <- 1:demoArresteeCount

twoTimeArrestID <- sample(arrestID, size = length(arrestID)*.64)
threeTimeArrestID <- sample(twoTimeArrestID, size = length(twoTimeArrestID)*.56)
fourTimeArrestID <- sample(threeTimeArrestID, size = length(threeTimeArrestID)*.68)
fiveTimeArrestID <- sample(fourTimeArrestID, size = length(fourTimeArrestID)*.49)

buildArrestRow <- function(ids) {
  
  n <- length(ids)
  
  date <- runif(n=n, min=0, max=364) + as.Date("2013-01-01")
  dateID <- format(date, DATE_ID_FORMAT)
  
  hours <- sample(0:23, size=n, replace=TRUE)
  minutes <- sample(0:59, size=n, replace=TRUE)
  seconds <- sample(0:59, size=n, replace=TRUE)
  timeID <- makeTimeID(hours, minutes, seconds)
  
  raceTypeID <- sample(1:5, size=n, replace=TRUE, prob=c(.3, .2, .4, .15, .05))
  
  ageID <- rchisq(n=n, df=23)
  for (i in 1:length(ageID)) {if (ageID[i] < 16) ageID[i] <- runif(min=16, max=23, n=1)}
  
  sexID <- sample(1:3, size=n, replace=TRUE, prob=c(.68, .29, .03))
  
  countyProb <- getCountyProbs()
  
  arresteeCountyID <- sample(countyID, size=n, replace=TRUE, prob=countyProb)
  arrestingAgencyID <- sample(agencyID, size=n, replace=TRUE)
  arrestPretrialServiceID <- sample(serviceID, size=n, replace=TRUE, prob=c(rep(.18, 5), .05, .05))
  violated <- sample(1:2, size=n, replace=TRUE, prob=c(.3, .7))
  drugRelated <- sample(1:2, size=n, replace=TRUE, prob=c(.35, .65))
  
  coords <- getRandomCoordsInCounties(arresteeCountyID)
  ArrestLocationLatitude <- coords$lat
  ArrestLocationLongitude <- coords$long

  df <- data.frame(ArrestID=ids,
             DateID=as.integer(dateID),
             TimeID=timeID,
             ArresteeRaceID=raceTypeID,
             ArresteeAgeID=as.integer(ageID),
             ArresteeSexID=sexID,
             CountyID=arresteeCountyID,
             ArrestingAgencyID=arrestingAgencyID,
             PretrialServiceID=arrestPretrialServiceID,
             ViolatedConditionsOfRelease=violated,
             ArrestDrugRelated=drugRelated,
             ArrestLocationLatitude,
             ArrestLocationLongitude,
             temp_date=date)
  
}

arrest <- buildArrestRow(arrestID)

buildRearrestRow <- function(id, index, arrestDataFrame, idLookupField, depth) {
  
  lookupId <- id
  for(i in 1:depth) lookupId <- arrestDataFrame[arrestDataFrame$ArrestID==lookupId, idLookupField]
  newRowDf <- arrestDataFrame[arrestDataFrame$ArrestID==lookupId, ]
  
  newRowDf$ArrestID <- nrow(arrestDataFrame) + index
  newRowDf$PriorArrestID <- lookupId
  
  daysSincePriorArrest <- rgamma(n=1, shape=15, scale=20)
  
  newRowDf$temp_date <- newRowDf$temp_date + daysSincePriorArrest
  newRowDf$DateID <- as.integer(format(newRowDf$temp_date, DATE_ID_FORMAT))
  
  hours <- sample(0:23, size=1, replace=TRUE)
  minutes <- sample(0:59, size=1, replace=TRUE)
  seconds <- sample(0:59, size=1, replace=TRUE)
  timeID <- makeTimeID(hours, minutes, seconds)
  
  newRowDf$TimeID <- timeID
  newRowDf$ArresteeAgeID <- as.integer((runif(1, 0, 364) + daysSincePriorArrest)/365) + newRowDf$ArresteeAgeID
  
  if (rbinom(prob=.15, n=1, size=1)) {
    # 15% of the time someone is arrested by a different agency in a different county
    newRowDf$CountyID <- sample(arrestDataFrame$CountyID, size=1)
    newRowDf$ArrestingAgencyID <- sample(arrestDataFrame$ArrestingAgencyID, size=1)
  }
  
  coords <- getRandomCoordsInCounty(as.character(newRowDf$CountyID))
  newRowDf$ArrestLocationLatitude <- coords[1,"lat"]
  newRowDf$ArrestLocationLongitude <- coords[1,"long"]
  
  newRowDf$PretrialServiceID <- sample(serviceID, size=1, prob=c(rep(.18, 5), .05, .05))
  newRowDf$ViolatedConditionsOfRelease <- sample(1:2, size=1, prob=c(.3, .7))
  newRowDf$ViolatedConditionsOfRelease <- sample(1:2, size=1, prob=c(.35, .65))

  newRowDf$SubsequentArrestID <- as.integer(NA)
  
  newRowDf
  
}

twoTimeArrest <- bind_rows(Map(buildRearrestRow, twoTimeArrestID, 1:length(twoTimeArrestID), MoreArgs=list(arrest, "ArrestID", 1)))
newArrest <- left_join(arrest, select(twoTimeArrest, SubsequentArrestID=ArrestID, ArrestID=PriorArrestID), by = c("ArrestID"))
arrest <- rbind(newArrest, select(twoTimeArrest, -(PriorArrestID)))

threeTimeArrest <- bind_rows(Map(buildRearrestRow, threeTimeArrestID, 1:length(threeTimeArrestID), MoreArgs=list(arrest, "SubsequentArrestID", 1)))
newArrest <- left_join(arrest, select(threeTimeArrest, SubsequentArrestID=ArrestID, ArrestID=PriorArrestID), by = c("ArrestID"))
newArrest$SubsequentArrestID <- newArrest$SubsequentArrestID.x
newArrest$SubsequentArrestID[!is.na(newArrest$SubsequentArrestID.y)] <- newArrest$SubsequentArrestID.y[!is.na(newArrest$SubsequentArrestID.y)]
newArrest <- select(newArrest, -contains('.'))
arrest <- rbind(newArrest, select(threeTimeArrest, -(PriorArrestID)))

fourTimeArrest <- bind_rows(Map(buildRearrestRow, fourTimeArrestID, 1:length(fourTimeArrestID), MoreArgs=list(arrest, "SubsequentArrestID", 2)))
newArrest <- left_join(arrest, select(fourTimeArrest, SubsequentArrestID=ArrestID, ArrestID=PriorArrestID), by = c("ArrestID"))
newArrest$SubsequentArrestID <- newArrest$SubsequentArrestID.x
newArrest$SubsequentArrestID[!is.na(newArrest$SubsequentArrestID.y)] <- newArrest$SubsequentArrestID.y[!is.na(newArrest$SubsequentArrestID.y)]
newArrest <- select(newArrest, -contains('.'))
arrest <- rbind(newArrest, select(fourTimeArrest, -(PriorArrestID)))

fiveTimeArrest <- bind_rows(Map(buildRearrestRow, fiveTimeArrestID, 1:length(fiveTimeArrestID), MoreArgs=list(arrest, "SubsequentArrestID", 3)))
newArrest <- left_join(arrest, select(fiveTimeArrest, SubsequentArrestID=ArrestID, ArrestID=PriorArrestID), by = c("ArrestID"))
newArrest$SubsequentArrestID <- newArrest$SubsequentArrestID.x
newArrest$SubsequentArrestID[!is.na(newArrest$SubsequentArrestID.y)] <- newArrest$SubsequentArrestID.y[!is.na(newArrest$SubsequentArrestID.y)]
newArrest <- select(newArrest, -contains('.'))
arrest <- rbind(newArrest, select(fiveTimeArrest, -(PriorArrestID)))

arrest <- select(arrest, -(temp_date))

dbWriteTable(conn, "Arrest", arrest, append=TRUE, row.names=FALSE)

#
# Charge
#

makeCharges <- function(arrestID) {
  getDispoOffense <- function(offenseTypeID) {
    dispoOffenseTypeID <- offenseTypeID
    if (rbinom(n=1, prob=.4, size=1)) {
      dispoOffenseTypeID <- sample(offenseID, size=1)
    }
    dispoOffenseTypeID
  }
  makeCharge <- function(arrestID) {
    offenseTypeID <- sample(offenseID, size=1)
    dispoOffenseTypeID <- getDispoOffense(offenseTypeID)
    arrestDispo <- sample(dispoID, size=1)
    dateID <- as.character(arrest[arrest$ArrestID==arrestID, "DateID"])
    arrestDate <- as.Date(dateID, DATE_ID_FORMAT)
    DispositionDate <- arrestDate + runif(n=1, min=60, max=400)
    DispositionDateID <- format(DispositionDate, DATE_ID_FORMAT)
    fine <- NA
    days <- NA
    if (arrestDispo == 1) {
      fine <- sample(size=1, c(0, 1, 50, 100, 500, 1000, 10000), replace=TRUE, prob=c(.25, .2, .15, .15, .1, .075, .075))
      days <- sample(size=1, c(0, 30, 90, 364, 5*365, 10*365, 25*365), replace=TRUE, prob=c(.4, .2, .2, .1, .05, .025, .025))
      if (fine == 0 && days == 0) {
        fine = 10
      }
    }
    chargeID <- NA
    data.frame(ChargeID=chargeID, ArrestOffenseTypeID=offenseTypeID, ArrestID=arrestID, SentenceTermDays=days,
               SentenceFineAmount=fine, DispositionID=arrestDispo, DispositionDateID, DispositionOffenseTypeID=dispoOffenseTypeID)
  }
  df <- makeCharge(arrestID)
  if (rbinom(n = 1, prob = .4, size = 1)) {
    df <- rbind(df, makeCharge(arrestID))
    if (rbinom(n = 1, prob = .3, size = 1)) {
      df <- rbind(df, makeCharge(arrestID))
    }
  }
  df
}

charges <- bind_rows(Map(makeCharges, arrest$ArrestID))
charges$ChargeID <- 1:nrow(charges)

dbWriteTable(conn, "Charge", as.data.frame(charges), append=TRUE, row.names=FALSE)

#
# PretrialParticipation
#

makePretrialParticipation <- function(arrestID) {
  PretrialServiceID <- sample(serviceID, size=1, prob=c(rep(.18, 5), .05, .05))
  countyProb <- getCountyProbs()
  CountyID <- sample(countyID, size=1, prob=countyProb)
  dateID <- as.character(arrest[arrest$ArrestID==arrestID, "DateID"])
  arrestDate <- as.Date(dateID, DATE_ID_FORMAT)
  IntakeDate <- arrestDate + runif(n=1, min=1, max=30)
  IntakeDateID <- format(IntakeDate, DATE_ID_FORMAT)
  AssessedNeedID <- sample(1:5, size=1, prob=c(.2, .2, .15, .4, .05))
  RiskScoreID <- sample(riskScoreID, size=1)
  PretrialServiceParticipationID <- NA
  data.frame(PretrialServiceParticipationID, PretrialServiceID, CountyID, IntakeDateID, RiskScoreID, AssessedNeedID, ArrestID=arrestID)
}
pretrialParticipants <- sample(arrest$ArrestID, nrow(arrest)*.75)
PretrialServiceParticipation <- bind_rows(Map(makePretrialParticipation, pretrialParticipants))
PretrialServiceParticipation$PretrialServiceParticipationID <- 1:nrow(PretrialServiceParticipation)

dbWriteTable(conn, "PretrialServiceParticipation", as.data.frame(PretrialServiceParticipation), append=TRUE, row.names=FALSE)

#
# Incident
#

IncidentID <- 1:demoIncidentCount
DateID <- format(runif(n=demoIncidentCount, min=0, max=364) + as.Date("2013-01-01"), DATE_ID_FORMAT)
hours <- sample(0:23, size=demoIncidentCount, replace=TRUE)
minutes <- sample(0:59, size=demoIncidentCount, replace=TRUE)
seconds <- sample(0:59, size=demoIncidentCount, replace=TRUE)
TimeID <- makeTimeID(hours, minutes, seconds)
IncidentTypeID <- sample(length(IncidentType), size=demoIncidentCount, replace=TRUE)
ReportingAgencyID=sample(agencyID, size=demoIncidentCount, replace=TRUE)
countyProb <- getCountyProbs()
CountyID <- sample(countyID, size=demoIncidentCount, prob=countyProb, replace = T)
coords <- getRandomCoordsInCounties(CountyID)
IncidentLocationLatitude <- coords$lat
IncidentLocationLongitude <- coords$long

Incident <- data.table(IncidentID, DateID, TimeID, ReportingAgencyID, IncidentTypeID,
                       CountyID, IncidentLocationLatitude, IncidentLocationLongitude)
dbWriteTable(conn, "Incident", Incident, append=TRUE, row.names=FALSE)

dbDisconnect(conn)
