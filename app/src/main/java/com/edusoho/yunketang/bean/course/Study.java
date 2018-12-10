package com.edusoho.yunketang.bean.course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by remilia on 2017/1/10.
 * 需要删掉，没用的class member 太多
 */
public class Study implements Serializable {

    private int total;
    private List<Resource> resources;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public static class Resource {
        private int learnedNum;
        private int totalLesson;
        private int id;
        private String title;
        private String subtitle;
        private String status;
        private String buyable;
        private String buyExpiryTime;
        private String type;
        private String maxStudentNum;
        private String price;
        private String originPrice;
        private String coinPrice;
        private String originCoinPrice;
        private String expiryMode;
        private String expiryDay;
        private String showStudentNumType;
        private String serializeMode;
        private String income;
        private String lessonNum;
        private String giveCredit;
        private String rating;
        private String ratingNum;
        private String vipLevelId;
        private String useInClassroom;
        private String singleBuy;
        private String categoryId;
        private String smallPicture;
        private String middlePicture;
        private String largePicture;
        private String about;
        private String recommended;
        private String recommendedSeq;
        private String recommendedTime;
        private String locationId;
        private String parentId;
        private String address;
        private String studentNum;
        private String hitNum;
        private String noteNum;
        private String userId;
        private String deadlineNotify;
        private String daysOfNotifyBeforeDeadline;
        private String watchLimit;
        private String createdTime;
        private String updatedTime;
        private String freeStartTime;
        private String freeEndTime;
        private String discountId;
        private String discount;
        private String approval;
        private String locked;
        private String maxRate;
        private String tryLookable;
        private String tryLookTime;
        private String orgCode;
        private String orgId;
        private String joinedType;
        private String convNo;
        private String classroomTitle;
        public int liveState;

        private List<String> teacherIds;
        private List<String> goals;
        private List<String> audiences;
        private List<?> tags;

        public int getLearnedNum() {
            return learnedNum;
        }

        public void setLearnedNum(int learnedNum) {
            this.learnedNum = learnedNum;
        }

        public int getTotalLesson() {
            return totalLesson;
        }

        public void setTotalLesson(int totalLesson) {
            this.totalLesson = totalLesson;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBuyable() {
            return buyable;
        }

        public void setBuyable(String buyable) {
            this.buyable = buyable;
        }

        public String getBuyExpiryTime() {
            return buyExpiryTime;
        }

        public void setBuyExpiryTime(String buyExpiryTime) {
            this.buyExpiryTime = buyExpiryTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMaxStudentNum() {
            return maxStudentNum;
        }

        public void setMaxStudentNum(String maxStudentNum) {
            this.maxStudentNum = maxStudentNum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }

        public String getCoinPrice() {
            return coinPrice;
        }

        public void setCoinPrice(String coinPrice) {
            this.coinPrice = coinPrice;
        }

        public String getOriginCoinPrice() {
            return originCoinPrice;
        }

        public void setOriginCoinPrice(String originCoinPrice) {
            this.originCoinPrice = originCoinPrice;
        }

        public String getExpiryMode() {
            return expiryMode;
        }

        public void setExpiryMode(String expiryMode) {
            this.expiryMode = expiryMode;
        }

        public String getExpiryDay() {
            return expiryDay;
        }

        public void setExpiryDay(String expiryDay) {
            this.expiryDay = expiryDay;
        }

        public String getShowStudentNumType() {
            return showStudentNumType;
        }

        public void setShowStudentNumType(String showStudentNumType) {
            this.showStudentNumType = showStudentNumType;
        }

        public String getSerializeMode() {
            return serializeMode;
        }

        public void setSerializeMode(String serializeMode) {
            this.serializeMode = serializeMode;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getLessonNum() {
            return lessonNum;
        }

        public void setLessonNum(String lessonNum) {
            this.lessonNum = lessonNum;
        }

        public String getGiveCredit() {
            return giveCredit;
        }

        public void setGiveCredit(String giveCredit) {
            this.giveCredit = giveCredit;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRatingNum() {
            return ratingNum;
        }

        public void setRatingNum(String ratingNum) {
            this.ratingNum = ratingNum;
        }

        public String getVipLevelId() {
            return vipLevelId;
        }

        public void setVipLevelId(String vipLevelId) {
            this.vipLevelId = vipLevelId;
        }

        public String getUseInClassroom() {
            return useInClassroom;
        }

        public void setUseInClassroom(String useInClassroom) {
            this.useInClassroom = useInClassroom;
        }

        public String getSingleBuy() {
            return singleBuy;
        }

        public void setSingleBuy(String singleBuy) {
            this.singleBuy = singleBuy;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getSmallPicture() {
            return smallPicture;
        }

        public void setSmallPicture(String smallPicture) {
            this.smallPicture = smallPicture;
        }

        public String getMiddlePicture() {
            return middlePicture;
        }

        public void setMiddlePicture(String middlePicture) {
            this.middlePicture = middlePicture;
        }

        public String getLargePicture() {
            int schemIndex = largePicture.lastIndexOf("http://");
            if (schemIndex != -1) {
                return largePicture.substring(schemIndex);
            }
            if (largePicture.startsWith("//")) {
                return "http:" + largePicture;
            }
            return largePicture;
        }

        public void setLargePicture(String largePicture) {
            this.largePicture = largePicture;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getRecommended() {
            return recommended;
        }

        public void setRecommended(String recommended) {
            this.recommended = recommended;
        }

        public String getRecommendedSeq() {
            return recommendedSeq;
        }

        public void setRecommendedSeq(String recommendedSeq) {
            this.recommendedSeq = recommendedSeq;
        }

        public String getRecommendedTime() {
            return recommendedTime;
        }

        public void setRecommendedTime(String recommendedTime) {
            this.recommendedTime = recommendedTime;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }

        public String getHitNum() {
            return hitNum;
        }

        public void setHitNum(String hitNum) {
            this.hitNum = hitNum;
        }

        public String getNoteNum() {
            return noteNum;
        }

        public void setNoteNum(String noteNum) {
            this.noteNum = noteNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDeadlineNotify() {
            return deadlineNotify;
        }

        public void setDeadlineNotify(String deadlineNotify) {
            this.deadlineNotify = deadlineNotify;
        }

        public String getDaysOfNotifyBeforeDeadline() {
            return daysOfNotifyBeforeDeadline;
        }

        public void setDaysOfNotifyBeforeDeadline(String daysOfNotifyBeforeDeadline) {
            this.daysOfNotifyBeforeDeadline = daysOfNotifyBeforeDeadline;
        }

        public String getWatchLimit() {
            return watchLimit;
        }

        public void setWatchLimit(String watchLimit) {
            this.watchLimit = watchLimit;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String getFreeStartTime() {
            return freeStartTime;
        }

        public void setFreeStartTime(String freeStartTime) {
            this.freeStartTime = freeStartTime;
        }

        public String getFreeEndTime() {
            return freeEndTime;
        }

        public void setFreeEndTime(String freeEndTime) {
            this.freeEndTime = freeEndTime;
        }

        public String getDiscountId() {
            return discountId;
        }

        public void setDiscountId(String discountId) {
            this.discountId = discountId;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getApproval() {
            return approval;
        }

        public void setApproval(String approval) {
            this.approval = approval;
        }

        public String getLocked() {
            return locked;
        }

        public void setLocked(String locked) {
            this.locked = locked;
        }

        public String getMaxRate() {
            return maxRate;
        }

        public void setMaxRate(String maxRate) {
            this.maxRate = maxRate;
        }

        public String getTryLookable() {
            return tryLookable;
        }

        public void setTryLookable(String tryLookable) {
            this.tryLookable = tryLookable;
        }

        public String getTryLookTime() {
            return tryLookTime;
        }

        public void setTryLookTime(String tryLookTime) {
            this.tryLookTime = tryLookTime;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getJoinedType() {
            return joinedType;
        }

        public void setJoinedType(String joinedType) {
            this.joinedType = joinedType;
        }

        public String getConvNo() {
            return convNo;
        }

        public void setConvNo(String convNo) {
            this.convNo = convNo;
        }

        public String getClassroomTitle() {
            return classroomTitle;
        }

        public void setClassroomTitle(String classroomTitle) {
            this.classroomTitle = classroomTitle;
        }

        public List<String> getTeacherIds() {
            return teacherIds;
        }

        public void setTeacherIds(List<String> teacherIds) {
            this.teacherIds = teacherIds;
        }

        public List<String> getGoals() {
            return goals;
        }

        public void setGoals(List<String> goals) {
            this.goals = goals;
        }

        public List<String> getAudiences() {
            return audiences;
        }

        public void setAudiences(List<String> audiences) {
            this.audiences = audiences;
        }

        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }
    }
}
