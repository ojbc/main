@echo off
REM Set NIEM Domains
SET core=ansi_d20 ansi-nist apco atf census dea dod_jcs-pub2.0-misc edxl edxl-cap edxl-de fbi fips_5-2 fips_6-4 fips_10-4 geospatial have hazmat iso_639-3 iso_3166 iso_4217 itis lasd mmucc_2 mn_offense niem-core nlets nonauthoritative-code post-canada sar twpdes ucr unece_rec20-misc usps_states ut_offender-tracking-misc
SET core2=appinfo proxy structures
SET domain=emergencyManagement immigration infrastructureProtection intelligence internationalTrade screening
SET jxdm=jxdm

REM Set NIEM Version
SET nv=2.0

REM Set JXDM domain version
SET jv=4.0

REM Set path to NIEM subset
SET s=..\Subset

REM Set transformation scripts
SET x1=make-constraint-schema-transform.xsl
SET x2=constraint-schema-transform.xsl
SET x3=cscript //d //nologo msxml.js