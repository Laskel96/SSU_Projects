credit <- read.csv("~/Dataset/credit.csv")

set.seed(1)

train_sample <- sample(1000, 700)

credit_train <- credit[train_sample, ]
credit_test  <- credit[-train_sample, ]

prop.table(table(credit_train$default))
prop.table(table(credit_test$default))

library(e1071)
credit_train$default <- as.factor(credit_train$default)
#nb_model <- naiveBayes(credit_train[-17], credit_train$default)
nb_model <- naiveBayes(credit_train$default~., data = credit_train)
nb_model

nb_pred <- predict(nb_model,credit_test)
library(gmodels)
CrossTable(credit_test$default, nb_pred,prop.chisq = FALSE, prop.t = FALSE, prop.r = FALSE,dnn = c('actual default', 'predicted default'))
