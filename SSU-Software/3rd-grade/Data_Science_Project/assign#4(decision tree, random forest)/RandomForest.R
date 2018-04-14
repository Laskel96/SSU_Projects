##
## Random Forests
##

library(randomForest)
set.seed(300)

credit <- read.csv("~/credit.csv")

ind <- sample(2, nrow(credit), replace = TRUE, prob = c(0.7, 0.3))

credit_train <- credit[ind == 1,]
credit_test <- credit[ind == 2,]

credit$default<-as.factor(credit$default)
credit_train$default <- as.factor(credit_train$default)
credit_test$default <- as.factor(credit_test$default)


rf <- randomForest(default ~ ., data = credit_train)

# Using Caret package random forests
library(caret)
set.seed(300)

ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid_rf <- expand.grid(.mtry = c(2, 4, 8, 16, 24, 32, 40))
#grid_rf <- expand.grid(.mtry = c(2, 4, 8, 16))

m_rf <- train(default ~ ., data = credit_train, method = "rf",
              metric = "Kappa", trControl = ctrl,
              tuneGrid = grid_rf)

# Use C5.0 Decision Tree to compare with random forests
grid_c50 <- expand.grid(.model = "tree",
                        .trials = c(10, 20, 30, 40, 50, 60),
                        .winnow = "FALSE")
#grid_c50 <- expand.grid(.model = "tree",
#                        .trials = c(10, 20, 30),
#                        .winnow = "FALSE")
set.seed(300)
m_c50 <- train(default ~ ., data = credit_train, method = "C5.0",
               metric = "Kappa", trControl = ctrl, tuneGrid = grid_c50)
rf
m_rf
m_c50


Crf_pred <- predict(m_rf, credit_test)
Srf_pred <- predict(rf, credit_test)
dt_pred <- predict(m_c50, credit_test)

confusionMatrix(Srf_pred, credit_test$default)
confusionMatrix(Crf_pred , credit_test$default)
confusionMatrix(dt_pred , credit_test$default)

