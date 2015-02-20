# Loads the staging database with dummy/demo data

demoArrestCount = 2000
demoIncidentCount = 2500
demoPretrialServiceParticipantCount = demoArrestCount*.4

startDateTime <- as.POSIXct("2012-01-01 00:00:00")
samplePeriodInDays <- 500

library(RMySQL)
library(data.table)

conn <- dbConnect(MySQL(), host="localhost", dbname="ojbc_staging_demo", username="root")

# update date column types...only need to run these if you drop and recreate the database
# this is necessary because SQL Power Architect does not support DATETIME fields...
# doesn't hurt to rerun these every time (MySQL handles it gracefully if the cols are already DATETIMEs)

dbSendQuery(conn, "alter table Incident modify IncidentDate DATETIME")
dbSendQuery(conn, "alter table ArrestCharge modify ArrestDate DATETIME")

# clear out code tables
dbSendQuery(conn, "delete from Agency")
dbSendQuery(conn, "delete from County")
dbSendQuery(conn, "delete from Disposition")
dbSendQuery(conn, "delete from OffenseType")
dbSendQuery(conn, "delete from PersonRace")
dbSendQuery(conn, "delete from PersonSex")
dbSendQuery(conn, "delete from PretrialService")
dbSendQuery(conn, "delete from RiskScore")
dbSendQuery(conn, "delete from AssessedNeed")
dbSendQuery(conn, "delete from IncidentType")

# clear out fact tables
dbSendQuery(conn, "delete from Person")
dbSendQuery(conn, "delete from Incident")
dbSendQuery(conn, "delete from ArrestCharge")
dbSendQuery(conn, "delete from ArrestChargeDisposition")
dbSendQuery(conn, "delete from PretrialServiceParticipation")

sexes <- c("Male","Female","Unknown")
PersonSex <- data.table(PersonSexID=1:length(sexes), PersonSexDescription=sexes)
dbWriteTable(conn, "PersonSex", PersonSex, append=TRUE, row.names=FALSE)

races <- c("BLACK","ASIAN","WHITE","UNKNOWN","AMERICAN INDIAN")
PersonRace <- data.table(PersonRaceID=1:length(races), PersonRaceDescription=races)
dbWriteTable(conn, "PersonRace", PersonRace, append=TRUE, row.names=FALSE)

incidentType <- c("Incident Type A", "Incident Type B", "Unknown")
incidentTypeID <- 1:length(incidentType)
IncidentType <- data.table(IncidentTypeID=incidentTypeID, IncidentTypeDescription=incidentType)
dbWriteTable(conn, "IncidentType", IncidentType, append=TRUE, row.names=FALSE)

need <- c("Need 1","Need 2", "Need 3", "None")
AssessedNeed <- data.table(AssessedNeedID=1:length(need), AssessedNeedDescription=need)
dbWriteTable(conn, "AssessedNeed", AssessedNeed, append=TRUE, row.names=FALSE)

countyID <- c(50001, 50003, 50005, 50007, 50009, 50011, 50013, 50015, 50017, 50019, 50021, 50023, 50025, 50027, 99999)
countyName <- c("ADDISON", "BENNINGTON", "CALEDONIA", "CHITTENDEN", "ESSEX", "FRANKLIN", "GRAND ISLE", "LAMOILLE", "ORANGE", "ORLEANS", "RUTLAND", "WASHINGTON", "WINDHAM", "WINDSOR", "Unknown")
County <- data.table(CountyID=countyID, CountyName=countyName)
dbWriteTable(conn, "County", County, append=TRUE, row.names=FALSE)

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

serviceID <- 1:5
serviceDescription <- paste("service", serviceID)
PretrialService <- data.table(PretrialServiceID=serviceID, PretrialServiceDescription=serviceDescription)
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

sampleSeconds <- runif(min = 0, max = samplePeriodInDays*24*60*60, n = demoIncidentCount)
incidentDates <- startDateTime + sampleSeconds

Incident <- data.table(IncidentID=1:demoIncidentCount, ReportingAgencyID=sample(agencyID, size = demoIncidentCount, replace = T),
                       IncidentDate=incidentDates, IncidentTypeID=sample(incidentTypeID, size = demoIncidentCount, replace = T))
dbWriteTable(conn, "Incident", Incident, append=TRUE, row.names=FALSE)

personCount <- demoArrestCount
personID <- 1:personCount
lastName <- paste("Last", personID, sep="")
firstName <- paste("First", personID, sep="")
middleName <- paste("Middle", personID, sep="")
ageInDays <- as.integer(rnorm(n=personCount, mean = 40*365, sd = 10*365))
ageInDays[(ageInDays/365) < 16] <- NA
birthdate <- as.Date(startDateTime) + samplePeriodInDays - ageInDays
raceTypeID <- sample(1:5, size=personCount, replace=TRUE, prob=c(.3, .2, .4, .15, .05))
sexID <- sample(1:3, size=personCount, replace=TRUE, prob=c(.68, .29, .03))

basePeople <- sample(personID, size = personCount*.7)
twoTimers <- sample(basePeople, size = length(basePeople)*.35)
threeTimers <- sample(twoTimers, size = length(twoTimers)*.2)
fourTimers <- sample(threeTimers, size = personCount - (length(basePeople) + length(twoTimers) + length(threeTimers)))
pretrialServiceParticipants <- sample(personID, size = demoPretrialServiceParticipantCount)

personID <- c(basePeople, twoTimers, threeTimers, fourTimers, pretrialServiceParticipants)

personCount <- length(personID)

biometricID <- 1:personCount
biometricID[sample(biometricID, size = personCount*.4)] <- NA

Person <- data.table(PersonID=1:personCount, PersonLastName=lastName[personID], PersonFirstName=firstName[personID],
                     PersonMiddleName=middleName[personID], PersonBirthDate=birthdate[personID],
                     PersonRaceID=raceTypeID[personID], PersonSexID=sexID[personID], PersonBiometricIdentifier=biometricID[personID])
dbWriteTable(conn, "Person", Person, append=TRUE, row.names=FALSE)

arrestChargeID <- 1:demoArrestCount
offenseTypeID <- sample(offenseID, size=demoArrestCount, replace=TRUE)

countyProb <- rep(.05, 15)
countyProb[4] <- .3 # chittenden

arresteeCountyID <- sample(countyID, size=demoArrestCount, replace=TRUE, prob=countyProb)

arrestingAgencyID <- sample(agencyID, size=demoArrestCount, replace=TRUE)

violated <- sample(1:2, size=demoArrestCount, replace=TRUE, prob=c(.3, .7))
drugRelated <- sample(1:2, size=demoArrestCount, replace=TRUE, prob=c(.35, .65))

sampleSeconds <- runif(min = 0, max = samplePeriodInDays*24*60*60, n = demoArrestCount)
arrestDates <- startDateTime + sampleSeconds

# it works to take the first demoArrestCount people, because they were combined first into the personID vector

ArrestCharge <- data.table(ArrestChargeID=arrestChargeID, PersonID=1:demoArrestCount, OffenseTypeID=offenseTypeID,
                           CountyID=arresteeCountyID, ArrestingAgencyID=arrestingAgencyID, ViolatedReleaseConditions=violated,
                           OffenseDrugRelated=drugRelated, ArrestDate=arrestDates)
dbWriteTable(conn, "ArrestCharge", ArrestCharge, append=TRUE, row.names=FALSE)

# right now, we don't ensure that the pretrial service date is after the arrest date for that person.  if we need that
# at some point, this will have to get more complex

pretrialServiceParticipationID <- 1:demoPretrialServiceParticipantCount
pretrialServiceID <- sample(serviceID, size=demoPretrialServiceParticipantCount, replace=TRUE)
assessedNeedID <- sample(1:length(need), size=demoPretrialServiceParticipantCount, replace=TRUE)
riskScoreID <- sample(riskScoreID, size=demoPretrialServiceParticipantCount, replace=TRUE)

sampleSeconds <- runif(min = 0, max = samplePeriodInDays*24*60*60, n = demoPretrialServiceParticipantCount)
intakeDates <- startDateTime + sampleSeconds

PretrialServiceParticipation <- data.table(PretrialServiceParticipationID=pretrialServiceParticipationID,
                                           PretrialServiceID=pretrialServiceID, IntakeDate=intakeDates,
                                           AssessedNeedID=assessedNeedID, RiskScoreID=riskScoreID,
                                           PersonID=(personCount-demoPretrialServiceParticipantCount+1):personCount)
dbWriteTable(conn, "PretrialServiceParticipation", PretrialServiceParticipation, append=TRUE, row.names=FALSE)

arrestChargeID <- sample(arrestChargeID, length(arrestChargeID)*.85)
arrestDispoCount <- length(arrestChargeID)
arrestChargeDispositionID <- 1:arrestDispoCount
dispositionID <- sample(dispoID, size=arrestDispoCount, replace=TRUE)

fine = rep(-1, arrestDispoCount)
days = rep(-1, arrestDispoCount)

for (i in 1:arrestDispoCount) {
  if (dispositionID[i] == 1) {
    fine[i] <- sample(size=1, c(0, 1, 50, 100, 500, 1000, 10000), replace=TRUE, prob=c(.25, .2, .15, .15, .1, .075, .075))
    days[i] <- sample(size=1, c(0, 30, 90, 364, 5*365, 10*365, 25*365), replace=TRUE, prob=c(.4, .2, .2, .1, .05, .025, .025))
    if (fine[i] == 0 && days[i] == 0) {
      fine[i] = 10
    }
  }
}

ArrestChargeDisposition <- data.table(ArrestChargeDispositionID=arrestChargeDispositionID,
                                      DispositionID=dispositionID, ArrestChargeID=arrestChargeID,
                                      SentenceTermDays=days, SentenceFineAmount=fine)
dbWriteTable(conn, "ArrestChargeDisposition", ArrestChargeDisposition, append=TRUE, row.names=FALSE)

dbDisconnect(conn)
