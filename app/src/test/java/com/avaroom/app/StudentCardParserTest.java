package com.avaroom.app;

import static org.junit.Assert.assertEquals;

import com.avaroom.app.ui.student.StudentEkycFragment;

import org.junit.Test;

public class StudentCardParserTest {

    @Test
    public void parseStudentCard_sameLineName() {
        String raw = "TRƯỜNG ĐẠI HỌC BÁCH KHOA HÀ NỘI\nHọ và tên: Nguyễn Văn An\nNiên khóa: 2021 - 2025";
        String[] result = StudentEkycFragment.parseStudentCard(raw);
        assertEquals("Nguyễn Văn An", result[0]);
        assertEquals("TRƯỜNG ĐẠI HỌC BÁCH KHOA HÀ NỘI", result[1]);
        assertEquals("2021-2025", result[2]);
    }

    @Test
    public void parseStudentCard_nextLineName() {
        String raw = "Họ và tên:\nTrần Thị Bình\nTrường Đại học CNTT\nK66";
        String[] result = StudentEkycFragment.parseStudentCard(raw);
        assertEquals("Trần Thị Bình", result[0]);
        assertEquals("Trường Đại học CNTT", result[1]);
        assertEquals("K66", result[2]);
    }

    @Test
    public void parseStudentCard_headerDoesNotFalseMatchName() {
        String raw = "THẺ SINH VIÊN ĐH BÁCH KHOA\nHọ tên: Lê Văn Cường\n2022-2026";
        String[] result = StudentEkycFragment.parseStudentCard(raw);
        assertEquals("Lê Văn Cường", result[0]);
        assertEquals("THẺ SINH VIÊN ĐH BÁCH KHOA", result[1]);
        assertEquals("2022-2026", result[2]);
    }
}
