package com.avaroom.app.data.model.request;

import com.google.gson.annotations.SerializedName;

public class CreateAppointmentRequest {

    @SerializedName("room_id")
    private long roomId;

    @SerializedName("appointment_date")
    private String appointmentDate; // YYYY-MM-DD

    @SerializedName("start_time")
    private String startTime; // HH:mm

    @SerializedName("end_time")
    private String endTime; // HH:mm (optional)

    @SerializedName("student_note")
    private String studentNote;

    public CreateAppointmentRequest() {
    }

    public CreateAppointmentRequest(long roomId, String appointmentDate, String startTime, String endTime, String studentNote) {
        this.roomId = roomId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentNote = studentNote;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStudentNote() {
        return studentNote;
    }

    public void setStudentNote(String studentNote) {
        this.studentNote = studentNote;
    }
}
