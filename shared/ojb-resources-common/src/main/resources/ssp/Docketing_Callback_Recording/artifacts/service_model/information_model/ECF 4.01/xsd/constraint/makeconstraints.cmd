@echo off
REM Create NIEM 2.0 constraint schemas

REM Read configuration file
CALL config.cmd

REM Copy NIEM appinfo, external, proxy and structure schemas
FOR %%A in (%core2%) DO XCOPY %s%\niem\%%A niem\%%A /S /E /Q /Y

REM Create constraint schemas for NIEM core
FOR %%A IN (%core%) DO (
%x3% %s%/niem/%%A/%nv%/%%A.xsd niem/%%A/%nv%/%x2% niem/%%A/%nv%/%%A.xsd yes
)
REM Create constraint schemas for JXDM domain
%x3% %s%/niem/domains/%jxdm%/%jv%/%jxdm%.xsd niem/domains/%jxdm%/%jv%/%x2% niem/domains/%jxdm%/%jv%/%jxdm%.xsd yes

REM Create constraint schemas for other NIEM domains
FOR %%A IN (%domain%) DO (
%x3% %s%/niem/domains/%%A/%nv%/%%A.xsd niem/domains/%%A/%nv%/%x2% niem/domains/%%A/%nv%/%%A.xsd yes
)
