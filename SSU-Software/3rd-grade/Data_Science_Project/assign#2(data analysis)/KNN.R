library(class) #Has the knn function
set.seed(4948493) 
credit = read.csv("~/Dataset/credit.csv",header=TRUE)

num_acc <- numeric(30)

for(attr in 1:21){
  if(!is.numeric(credit[0,attr])){
    levels(credit[,attr]) <- 0:10
  }
}

credit_sample<-sample(1:nrow(credit),size=nrow(credit)*.7)
credit_train<-credit[credit_sample,] #Select the 70% of rows
credit_test<-credit[-credit_sample,] #Select the 30% of rows

#First Attempt to Determine Right K####
credit_acc<-numeric() #Holding variable
for(i in 1:30){
  #Apply knn with k = i
  predict<-knn(credit_train[,-17],credit_test[,-17], credit_train$default,k=i)
  credit_acc<-c(credit_acc, mean(predict==credit_test$default))
}
#Plot k= 1 through 30
plot(1-credit_acc,type="l",ylab="Error Rate",
     xlab="K",main="Error Rate for credit With Varying K")

#Try many Samples of Iris Data Set to Validate K####
trial_sum<-numeric(100)
trial_n<-numeric(100)
set.seed(6033850)
for(i in 1:10){
  credit_sample<-sample(1:nrow(credit),size=nrow(credit)*.7)
  credit_train<-credit[credit_sample,]
  credit_test<-credit[-credit_sample,]
  test_size<-nrow(credit_test)
  for(j in 1:100){
    predict<-knn(credit_train[,-17],credit_test[,-17],credit_train$default,k=j)
    trial_sum[j]<-trial_sum[j]+sum(predict==credit_test$default)
    trial_n[j]<-trial_n[j]+test_size
  }
}

plot(1-trial_sum / trial_n,type="l",ylab="Error Rate",
     xlab="K",main="Error Rate for credit With Varying K (100 Samples)")

best_credit_acc<-numeric()
best_predict<-knn(credit_train[,-17],credit_test[,-17],credit_train$default,k=50)
best_credit_acc <- c(best_credit_acc,mean(predict ==credit_test$default))
precision <- best_credit_acc#sum(predict & actual_labels) / sum(predict)
recall <- best_credit_acc#sum(predict & actual_labels) / sum(actual_labels)

fmeasure <- 2 * precision * recall / (precision + recall)

library(gmodels)

CrossTable(credit_test$default, best_predict,prop.chisq = FALSE, prop.t = FALSE, prop.r = FALSE,dnn = c('actual default', 'predicted default'))

