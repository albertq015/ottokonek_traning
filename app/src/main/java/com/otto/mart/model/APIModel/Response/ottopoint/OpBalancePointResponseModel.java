package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpBalancePointResponseModel extends BaseResponseOttopoint {

    public OpBalancePointResponseModel() {
    }

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        private String firstName;
        private String lastName;
        private String customerId;
        private String points;
        private long p2pPoints;
        private String totalEarnedPoints;
        private long usedPoints;
        private long expiredPoints;
        private long lockedPoints;
        private String level;
        private String levelName;
        private int levelConditionValue;
        private String nextLevel;
        private String nextLevelName;
        private int nextLevelConditionValue;
        private int transactionsAmountWithoutDeliveryCosts;
        private int transactionsAmountToNextLevel;
        private String averageTransactionsAmount;
        private int transactionsCount;
        private int transactionsAmount;
        private String currency;
        private String pointsExpiringNextMonth;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public long getP2pPoints() {
            return p2pPoints;
        }

        public void setP2pPoints(long p2pPoints) {
            this.p2pPoints = p2pPoints;
        }

        public String getTotalEarnedPoints() {
            return totalEarnedPoints;
        }

        public void setTotalEarnedPoints(String totalEarnedPoints) {
            this.totalEarnedPoints = totalEarnedPoints;
        }

        public long getUsedPoints() {
            return usedPoints;
        }

        public void setUsedPoints(long usedPoints) {
            this.usedPoints = usedPoints;
        }

        public long getExpiredPoints() {
            return expiredPoints;
        }

        public void setExpiredPoints(long expiredPoints) {
            this.expiredPoints = expiredPoints;
        }

        public long getLockedPoints() {
            return lockedPoints;
        }

        public void setLockedPoints(long lockedPoints) {
            this.lockedPoints = lockedPoints;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public int getLevelConditionValue() {
            return levelConditionValue;
        }

        public void setLevelConditionValue(int levelConditionValue) {
            this.levelConditionValue = levelConditionValue;
        }

        public String getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(String nextLevel) {
            this.nextLevel = nextLevel;
        }

        public String getNextLevelName() {
            return nextLevelName;
        }

        public void setNextLevelName(String nextLevelName) {
            this.nextLevelName = nextLevelName;
        }

        public int getNextLevelConditionValue() {
            return nextLevelConditionValue;
        }

        public void setNextLevelConditionValue(int nextLevelConditionValue) {
            this.nextLevelConditionValue = nextLevelConditionValue;
        }

        public int getTransactionsAmountWithoutDeliveryCosts() {
            return transactionsAmountWithoutDeliveryCosts;
        }

        public void setTransactionsAmountWithoutDeliveryCosts(int transactionsAmountWithoutDeliveryCosts) {
            this.transactionsAmountWithoutDeliveryCosts = transactionsAmountWithoutDeliveryCosts;
        }

        public int getTransactionsAmountToNextLevel() {
            return transactionsAmountToNextLevel;
        }

        public void setTransactionsAmountToNextLevel(int transactionsAmountToNextLevel) {
            this.transactionsAmountToNextLevel = transactionsAmountToNextLevel;
        }

        public String getAverageTransactionsAmount() {
            return averageTransactionsAmount;
        }

        public void setAverageTransactionsAmount(String averageTransactionsAmount) {
            this.averageTransactionsAmount = averageTransactionsAmount;
        }

        public int getTransactionsCount() {
            return transactionsCount;
        }

        public void setTransactionsCount(int transactionsCount) {
            this.transactionsCount = transactionsCount;
        }

        public int getTransactionsAmount() {
            return transactionsAmount;
        }

        public void setTransactionsAmount(int transactionsAmount) {
            this.transactionsAmount = transactionsAmount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPointsExpiringNextMonth() {
            return pointsExpiringNextMonth;
        }

        public void setPointsExpiringNextMonth(String pointsExpiringNextMonth) {
            this.pointsExpiringNextMonth = pointsExpiringNextMonth;
        }
    }
}
