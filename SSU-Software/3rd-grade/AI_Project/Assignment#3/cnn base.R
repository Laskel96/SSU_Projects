# test data sampling
train = read.csv("~/AI dataset/train.csv",header=TRUE)

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


# test data sampling
test = read.csv("~/AI dataset/mnist_test.csv",header=TRUE)

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

library(mxnet)

for(i in 1:1000){
  data <- mx.symbol.Variable(paste(unlist(trainSam[i,-1]),collapse=''))
}

  conv1 <- mx.symbol.Convolution(data = data, kernel = c(5,5), num_filter = 20)
  tanh1 <- mx.symbol.Activation(data = conv1, act_type="tanh")
  pool1 <- mx.symbol.Pooling(data=tanh1, pool_type="max", kernel=c(2,2),stride=c(2,2))

  conv2 <- mx.symbol.Convolution(data = pool1, kernel=c(5,5), num_filter=50)
  tanh2 <- mx.symbol.Activation(data = conv2, act_type="tanh")
  pool2 <- mx.symbol.Pooling(data=tanh2, pool_type="max",kernel=c(2,2),stride=c(2,2))

  flatten <- mx.symbol.Flatten(data=pool2)
  fc1 <- mx.symbol.FullyConnected(data=flatten, num_hidden = 500)
  tanh3 <- mx.symbol.Activation(data=fc1, act_type="tanh")

  fc2 <- mx.symbol.FullyConnected(data=tanh3, num_hidden = 10)

  lenet <- mx.symbol.SoftmaxOutput(data=fc2)
