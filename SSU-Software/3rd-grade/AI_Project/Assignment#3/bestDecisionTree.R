# test data sampling
trainSam = read.csv("~/AI dataset/train_comp.csv",header=TRUE)

# test data sampling
testSam = read.csv("~/AI dataset/test_comp.csv",header=TRUE)

trainSam$label = as.factor(trainSam$label)
testSam$label = as.factor(testSam$label)

num_acc <- numeric()

mod1 <- C5.0(trainSam[,-1], 
             trainSam$label, 
             trials = 10, 
             control = C5.0Control(minCases = 2,noGlobalPruning = TRUE)) #make decision tree
p1 <- predict(mod1, testSam) #predictsin
num_acc <- c(num_acc, mean(p1 == testSam$label)) #accuracy
precision <- num_acc#sum(predict & actual_labels) / sum(predict)
recall <- num_acc#sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)