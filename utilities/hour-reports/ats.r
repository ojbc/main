library(tidyRSS)
library(tidyverse)
library(readxl)

#after building the generateOJBCHoursReport all you need to do is run the following command and specify fund source and location of input file
generateOJBCHoursReport(700001, "data/sample.xlsx")



generateOJBCHoursReport <- function(fundSource, reportFile) {
#OJBC Fund sources
fundSourceID <- c(700000,700001,700002,700003, 700004, 700005, 700006, 700007, 700008,700009,700010,700011,700012,700013, 700999)
client <-c("OJBC Admin", "Hawaii", "Maine", "Vermont", "Arnold Foundation", "Michigan", "Ottawa County", "Adams County", 
               "Pima County","Montana", "Puerto Rico", "Oklahoma", "Alameda County, CA", "First Judicial Court NM", "OJBC Special Projects")

ojbcFSList <- as_tibble(data.frame(fundSourceID, client))

# extract project names and IDs from ATS
rawATSFeed <- tidyfeed("https://ats.search.org/ActivityTrackingSystem/rssFeed/feedType/New%20Project/")
projects <- rawATSFeed %>% 
  mutate(Project=str_sub(item_title, -24,-21)) %>% 
  separate(item_title, into = c("a", "b"), "Project ") %>% 
  separate(b, into=c("ProjectName","d"), " has") %>% 
  select(Project, ProjectName) %>% 
  mutate(ProjectName=str_replace(ProjectName, "[\\]]", "")) %>% 
  mutate(ProjectName=str_replace(ProjectName, "[\\[]", ""))
  
ojbcHours <- read_xlsx(reportFile) %>% 
  mutate(Project=as.character(Project)) %>% 
  mutate(fundSourceID=`Fund Source`) %>% 
  left_join(projects, by="Project") %>% 
  left_join(ojbcFSList, by="fundSourceID") %>% 
  arrange(client, ProjectName)

totals <- ojbcHours %>% 
  group_by(`Fund Source`, ProjectName) %>% 
  summarise(hours=sum(Regular)) %>% 
  filter(`Fund Source`== fundSource)

return(totals)
}

