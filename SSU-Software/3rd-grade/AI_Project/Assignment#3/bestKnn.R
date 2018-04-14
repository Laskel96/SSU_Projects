# test data sampling
trainSam = read.csv("~/AI dataset/train_comp.csv",header=TRUE)

# test data sampling
testSam = read.csv("~/AI dataset/test_comp.csv",header=TRUE)

trainSam$label = as.factor(trainSam$label)
testSam$label = as.factor(testSam$label)

library(class)

num_acc <- numeric()

predict <- knn(trainSam[,-1],testSam[,-1],trainSam$label,k=5)
num_acc <- c(num_acc,mean(predict == testSam$label))
precision <- num_acc#sum(predict & actual_labels) / sum(predict)
recall <- num_acc#sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)