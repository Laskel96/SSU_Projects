# test data sampling
train = read.csv("~/AI dataset/train.csv",header=TRUE)

# test data sampling
test = read.csv("~/AI dataset/mnist_test.csv",header=TRUE)

num_acc <- numeric(20)
fmeasure_acc <- numeric(20)

for(i in 1:20){
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



predict <-sample(c(0,1,2,3,4,5,6,7,8,9), size = 200, replace = TRUE, prob = c(0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1))
num_acc[i] <- mean(predict == testSam$label)
precision = num_acc[i]#sum(predict & actual_labels) / sum(predict)
recall = num_acc[i] #sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)
fmeasure_acc[i] <- fmeasure_acc[i] + fmeasure

}
plot(fmeasure_acc, type = "l", ylab = "fmeasure",
     Xlab = "K", main="iteration of basemodel")
