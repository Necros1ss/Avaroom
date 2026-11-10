package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class EkycResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private EkycData data;

    public EkycResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EkycData getData() {
        return data;
    }

    public void setData(EkycData data) {
        this.data = data;
    }

    // Chỉ chứa đúng 8 trường dữ liệu cá nhân (OCR)
    public static class EkycData implements Serializable {

        // 1. SỐ THẺ CCCD
        @SerializedName(value = "id_number", alternate = {"idNumber", "cccd_number", "cccdNumber"})
        private String idNumber;

        // 2. HỌ VÀ TÊN
        @SerializedName(value = "full_name", alternate = {"fullName"})
        private String fullName;

        // 3. NGÀY SINH
        @SerializedName(value = "date_of_birth", alternate = {"dateOfBirth", "dob", "birth_date"})
        private String dateOfBirth;

        // 4. GIỚI TÍNH
        @SerializedName(value = "gender", alternate = {"sex"})
        private String gender;

        // 5. QUÊ QUÁN
        @SerializedName(value = "place_of_origin", alternate = {"placeOfOrigin", "hometown", "origin_place"})
        private String placeOfOrigin;

        // 6. NƠI THƯỜNG TRÚ
        @SerializedName(value = "place_of_residence", alternate = {"placeOfResidence", "permanent_address", "permanentAddress", "address"})
        private String placeOfResidence;

        // 7. NGÀY CẤP
        @SerializedName(value = "issue_date", alternate = {"issueDate", "issued_date"})
        private String issueDate;

        // 8. NƠI CẤP
        @SerializedName(value = "issue_place", alternate = {"issuePlace", "issued_by", "issuedBy"})
        private String issuePlace;

        public EkycData() {
        }

        // --- GETTERS & SETTERS ---

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPlaceOfOrigin() {
            return placeOfOrigin;
        }

        public void setPlaceOfOrigin(String placeOfOrigin) {
            this.placeOfOrigin = placeOfOrigin;
        }

        public String getPlaceOfResidence() {
            return placeOfResidence;
        }

        public void setPlaceOfResidence(String placeOfResidence) {
            this.placeOfResidence = placeOfResidence;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getIssuePlace() {
            return issuePlace;
        }

        public void setIssuePlace(String issuePlace) {
            this.issuePlace = issuePlace;
        }
    }
}