#install.packages("arulesViz")  
library(arulesViz)
library(arules)
data("Groceries")

rules <- apriori(Groceries, parameter = list(supp = 0.01, conf = 0.3))
options(digits=3) #for 3 digits
rule_interest <- subset(rules, items %in% c("whole milk"))  # if contains A(item)




summary(rules)

itemFrequencyPlot(Groceries, topN = 20, type="absolute") #item frequency

inspect(sort(rules, by = "lift")[1:20]) # 20 item sorted by lift
inspect(sort(rule_interest, by = "lift")[1:20]) #20 items sorted by lift that contains A

plot(sort(rules, by = "lift")[1:20],method="graph",control=list(type="itemsets")) # 
plot(sort(rules, by = "lift")[1:20],method="paracoord") # forexample curd & milk = > yogurt

plot(sort(rule_interest, by = "lift")[1:20],method="graph",control=list(type="itemsets")) # 
plot(sort(rule_interest, by = "lift")[1:20],method="paracoord")
