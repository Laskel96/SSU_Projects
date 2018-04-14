#install.packages("rattle")
# Importing data
credit <- read.csv("~/Dataset/credit.csv")
# Resuffling the observation order and splitting into training & test sets

ind <- sample(2, nrow(credit), replace = TRUE, prob = c(0.7, 0.3))

credit_train <- credit[ind == 1,]
credit_test <- credit[ind == 2,]

library(C50)

credit_train$default<-as.factor(credit_train$default)

credit_model <- C5.0(x= credit_train[-17], y = credit_train$default)
credit_pred <- predict(credit_model, credit_test)

credit_model

error_cost <- matrix(c(0, 1, 4, 0), nrow = 2) # create a cost matrix

credit_cost <- C5.0(credit_train[-17], credit_train$default,costs = error_cost) # Apply the cost matrix to the tree

credit_cost_pred <- predict(credit_cost, credit_test)
library(gmodels)
CrossTable(credit_test$default, credit_cost_pred,prop.chisq = FALSE, prop.c = FALSE, prop.r = FALSE,dnn = c('actual default', 'predicted default'))