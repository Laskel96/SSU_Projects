# test data sampling
train = read.csv("~/AI dataset/train.csv",header=TRUE)

# test data sampling
test = read.csv("~/AI dataset/mnist_test.csv",header=TRUE)

#sampling count for each number
k = 100

#stratified sampling
only0 = subset(train, label == 0)
only1 = subset(train, label == 1)
only2 = subset(train, label == 2)
only3 = subset(train, label == 3)
only4 = subset(train, label == 4)
only5 = subset(train, label == 5)
only6 = subset(train, label == 6)
only7 = subset(train, label == 7)
only8 = subset(train, label == 8)
only9 = subset(train, label == 9)
  
  sam0 = only0[sample(nrow(only0)),][1:k,]
  sam1 = only1[sample(nrow(only1)),][1:k,]
  sam2 = only2[sample(nrow(only2)),][1:k,]
  sam3 = only3[sample(nrow(only3)),][1:k,]
  sam4 = only4[sample(nrow(only4)),][1:k,]
  sam5 = only5[sample(nrow(only5)),][1:k,]
  sam6 = only6[sample(nrow(only6)),][1:k,]
  sam7 = only7[sample(nrow(only7)),][1:k,]
  sam8 = only8[sample(nrow(only8)),][1:k,]
  sam9 = only9[sample(nrow(only9)),][1:k,]
  
  trainSam = rbind(sam0, sam1, sam2, sam3, sam4, sam5, sam6, sam7, sam8, sam9)
  trainSam = trainSam[sample(nrow(trainSam)),]
  trainSam$label = as.factor(trainSam$label)
  
  #sampling count for each number
  l = 20
  
  #stratified sampling
  only0 = subset(test, label == 0)
  only1 = subset(test, label == 1)
  only2 = subset(test, label == 2)
  only3 = subset(test, label == 3)
  only4 = subset(test, label == 4)
  only5 = subset(test, label == 5)
  only6 = subset(test, label == 6)
  only7 = subset(test, label == 7)
  only8 = subset(test, label == 8)
  only9 = subset(test, label == 9)
  
  sam0 = only0[sample(nrow(only0)),][1:l,]
  sam1 = only1[sample(nrow(only1)),][1:l,]
  sam2 = only2[sample(nrow(only2)),][1:l,]
  sam3 = only3[sample(nrow(only3)),][1:l,]
  sam4 = only4[sample(nrow(only4)),][1:l,]
  sam5 = only5[sample(nrow(only5)),][1:l,]
  sam6 = only6[sample(nrow(only6)),][1:l,]
  sam7 = only7[sample(nrow(only7)),][1:l,]
  sam8 = only8[sample(nrow(only8)),][1:l,]
  sam9 = only9[sample(nrow(only9)),][1:l,]
  
  testSam = rbind(sam0, sam1, sam2, sam3, sam4, sam5, sam6, sam7, sam8, sam9)
  testSam = testSam[sample(nrow(testSam)),]
  
  testSam$label = as.factor(testSam$label)

library(C50)

num_acc <- numeric()
t_num_acc <- numeric()
gp_num_acc <- numeric()
m_num_acc <- numeric()
#basic tree, trial = 1, control default is 1. noGlobalPruning =true, minCases = 2 

mod1 <- C5.0(trainSam[,-1], trainSam$label) #make decision tree
p1 <- predict(mod1, testSam) #predictsin
num_acc <- c(num_acc, mean(p1 == testSam$label)) #accuracy
precision <- num_acc#sum(predict & actual_labels) / sum(predict)
recall <- num_acc#sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)

#case of trials 1 to 10

for(i in 1:15){
  t_mod1 <- C5.0(trainSam[,-1], trainSam$label, trials = i)
  t_p1 <- predict(t_mod1, testSam)
  t_num_acc <- c(t_num_acc, mean(t_p1 == testSam$label))
  t_precision <- t_num_acc#sum(predict & actual_labels) / sum(predict)
  t_recall <- t_num_acc#sum(predict & actual_labels) / sum(actual_labels)
  
  t_fmeasure <- 2 * t_precision * t_recall / (t_precision + t_recall)
}

plot(t_fmeasure, type = "l", ylab = "fmeasure",
     Xlab = "i", main="fmeasure With trial")

#case of nogloabalPruning = false

gp_mod1 <- C5.0(trainSam[,-1], trainSam$label, control = C5.0Control(noGlobalPruning = FALSE))
gp_p1 <- predict(gp_mod1, testSam)
gp_num_acc <- c(gp_num_acc, mean(gp_p1 == testSam$label))
gp_precision <- gp_num_acc#sum(predict & actual_labels) / sum(predict)
gp_recall <- gp_num_acc#sum(predict & actual_labels) / sum(actual_labels)
gp_fmeasure <- 2 * gp_precision * gp_recall / (gp_precision + gp_recall)

#case of minCases 1 to 10

for(i in 1:10){
  m_mod1 <- C5.0(trainSam[,-1], trainSam$label, control = C5.0Control(minCases = i))
  m_p1 <- predict(m_mod1, testSam)
  m_num_acc <- c(m_num_acc, mean(m_p1 == testSam$label))
  m_precision <- m_num_acc#sum(predict & actual_labels) / sum(predict)
  m_recall <- m_num_acc#sum(predict & actual_labels) / sum(actual_labels)
  
  m_fmeasure <- 2 * m_precision * m_recall / (m_precision + m_recall)
}

plot(m_fmeasure, type = "l", ylab = "fmeasure",
     Xlab = "i", main="fmeasure With minCases")
