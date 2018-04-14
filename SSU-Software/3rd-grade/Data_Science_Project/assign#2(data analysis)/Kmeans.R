teens <- read.csv("~/Dataset/snsdata.csv")
teens <- na.omit(teens)
summary(teens$age) # check data
teens$age <- ifelse(teens$age >= 13 & teens$age < 20, teens$age, NA) # remove not suitable
summary(teens$age) # recheck data

library(stats)
interests <- teens[5:40] # binary
interests_z <- as.data.frame(lapply(interests, scale)) #z-score
teen_clusters <- kmeans(interests_z, 5)

#teen_clusters$size

#teen_clusters$centers

teens$cluster <- teen_clusters$cluster

teens[1:300, c("cluster","gender", "age", "friends", "basketball","football","soccer", "softball")]
teens[1:300, c("cluster","gender", "age", "friends")]

aggregate(data = teens, age ~ cluster, mean)
aggregate(data = teens, football ~ cluster, mean)
aggregate(data = teens, friends ~ cluster, mean)

aggregate(data = teens, church ~ cluster, mean)
aggregate(data = teens, sex ~ cluster, mean)


