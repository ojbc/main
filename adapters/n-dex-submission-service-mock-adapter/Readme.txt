You must add a host entry like this for example in /etc/hosts (or windows comparable):

127.0.0.1       ojbc.maine.gov 

Otherwise the N-DEx submission client will say something like 'localhost' does not match the name in the certificate. It is hard to directly disable this CN / Host name checking.

Also, we want this enabled for production N-DEx submissions.

