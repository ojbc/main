@echo off
REM Create XSL trasnformation scripts that will produce NIEM 2.0 constraint schemas

REM Read configuration file
CALL config.cmd

REM Create constraint schema directory structure
XCOPY %s% . /T /E /Q /Y

REM Create constraint transformations for NIEM core
FOR %%A IN (%core%) DO (
%x3% %s%/niem/%%A/%nv%/%%A.xsd %x1% niem/%%A/%nv%/%x2% no 
)
REM Create constraint transformation for JXDM domain
%x3% %s%/niem/domains/%jxdm%/%jv%/%jxdm%.xsd %x1% niem/domains/%jxdm%/%jv%/%x2% no

REM Create constraint transformations for other NIEM domains
FOR %%A IN (%domain%) DO (
%x3% %s%/niem/domains/%%A/%nv%/%%A.xsd %x1% niem/domains/%%A/%nv%/%x2% no
)
