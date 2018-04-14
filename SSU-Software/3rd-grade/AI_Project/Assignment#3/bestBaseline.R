# test data sampling
trainSam = read.csv("~/AI dataset/train_comp.csv",header=TRUE)

# test data sampling
testSam = read.csv("~/AI dataset/test_comp.csv",header=TRUE)

trainSam$label = as.factor(trainSam$label)
testSam$label = as.factor(testSam$label)

predict <-sample(c(0,1,2,3,4,5,6,7,8,9), size = 200, replace = TRUE, prob = c(0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1))
num_acc <- mean(predict == testSam$label)
precision <- num_acc#sum(predict & actual_labels) / sum(predict)
recall <- num_acc #sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)