package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.Timetable;
import com.nimblix.SchoolPEPProject.Repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class TimetableDataController {

    private final TimetableRepository timetableRepository;

    @PostMapping("/timetable/seed-data")
    public ResponseEntity<String> seedTimetableData() {
        List<Timetable> timetables = new ArrayList<>();

        // Monday timetable
        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("MONDAY")
                .periodNumber(1)
                .subject("Mathematics")
                .teacherName("Mr. Smith")
                .startTime("09:00 AM")
                .endTime("09:45 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("MONDAY")
                .periodNumber(2)
                .subject("English")
                .teacherName("Ms. Johnson")
                .startTime("09:50 AM")
                .endTime("10:35 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("MONDAY")
                .periodNumber(3)
                .subject("Science")
                .teacherName("Dr. Brown")
                .startTime("10:40 AM")
                .endTime("11:25 AM")
                .build());

        // Tuesday timetable
        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("TUESDAY")
                .periodNumber(1)
                .subject("Physics")
                .teacherName("Mr. Wilson")
                .startTime("09:00 AM")
                .endTime("09:45 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("TUESDAY")
                .periodNumber(2)
                .subject("Chemistry")
                .teacherName("Ms. Davis")
                .startTime("09:50 AM")
                .endTime("10:35 AM")
                .build());

        // Wednesday timetable
        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("WEDNESDAY")
                .periodNumber(1)
                .subject("History")
                .teacherName("Mr. Miller")
                .startTime("09:00 AM")
                .endTime("09:45 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("WEDNESDAY")
                .periodNumber(2)
                .subject("Geography")
                .teacherName("Ms. Garcia")
                .startTime("09:50 AM")
                .endTime("10:35 AM")
                .build());

        // Thursday timetable
        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("THURSDAY")
                .periodNumber(1)
                .subject("Computer Science")
                .teacherName("Mr. Anderson")
                .startTime("09:00 AM")
                .endTime("09:45 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("THURSDAY")
                .periodNumber(2)
                .subject("Physical Education")
                .teacherName("Mr. Taylor")
                .startTime("09:50 AM")
                .endTime("10:35 AM")
                .build());

        // Friday timetable
        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("FRIDAY")
                .periodNumber(1)
                .subject("Art")
                .teacherName("Ms. Martinez")
                .startTime("09:00 AM")
                .endTime("09:45 AM")
                .build());

        timetables.add(Timetable.builder()
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .dayOfWeek("FRIDAY")
                .periodNumber(2)
                .subject("Music")
                .teacherName("Mr. Robinson")
                .startTime("09:50 AM")
                .endTime("10:35 AM")
                .build());

        timetableRepository.saveAll(timetables);

        return ResponseEntity.ok("Sample timetable data seeded successfully! Added " + timetables.size() + " timetable entries.");
    }
}
