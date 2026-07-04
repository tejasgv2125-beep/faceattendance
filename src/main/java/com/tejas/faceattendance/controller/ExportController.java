package com.tejas.faceattendance.controller;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.service.AttendanceService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class ExportController {

    private final AttendanceService attendanceService;

    public ExportController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // ======================================================
    // Export Attendance as CSV
    // ======================================================

    @GetMapping("/attendance/export/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=attendance.csv"
        );

        List<Attendance> attendanceList =
                attendanceService.getAllAttendance();

        PrintWriter writer = response.getWriter();

        writer.println("ID,Student Name,USN,Department,Date,Time,Status");

        for (Attendance attendance : attendanceList) {

            writer.println(
                    attendance.getId() + "," +
                            attendance.getStudent().getName() + "," +
                            attendance.getStudent().getUsn() + "," +
                            attendance.getStudent().getDepartment() + "," +
                            attendance.getAttendanceDate() + "," +
                            attendance.getAttendanceTime() + "," +
                            attendance.getStatus()
            );

        }

        writer.flush();
        writer.close();

    }

    // ======================================================
    // Export Attendance as Excel
    // ======================================================

    @GetMapping("/attendance/export/excel")
    public void exportExcel(HttpServletResponse response)
            throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=attendance.xlsx");

        List<Attendance> attendanceList =
                attendanceService.getAllAttendance();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Attendance");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        String[] columns = {
                "ID",
                "Student Name",
                "USN",
                "Department",
                "Semester",
                "Email",
                "Phone",
                "Date",
                "Time",
                "Status"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = headerRow.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);

        }

        int rowNumber = 1;

        for (Attendance attendance : attendanceList) {

            Row row = sheet.createRow(rowNumber++);

            row.createCell(0).setCellValue(attendance.getId());
            row.createCell(1).setCellValue(attendance.getStudent().getName());
            row.createCell(2).setCellValue(attendance.getStudent().getUsn());
            row.createCell(3).setCellValue(attendance.getStudent().getDepartment());
            row.createCell(4).setCellValue(attendance.getStudent().getSemester());
            row.createCell(5).setCellValue(attendance.getStudent().getEmail());
            row.createCell(6).setCellValue(attendance.getStudent().getPhone());
            row.createCell(7).setCellValue(attendance.getAttendanceDate().toString());
            row.createCell(8).setCellValue(attendance.getAttendanceTime().toString());
            row.createCell(9).setCellValue(attendance.getStatus());

        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(outputStream);

        workbook.close();

        response.getOutputStream()
                .write(outputStream.toByteArray());

        response.getOutputStream().flush();

        response.getOutputStream().close();

    }

    // ======================================================
    // Export Attendance as PDF
    // ======================================================

    @GetMapping("/attendance/export/pdf")
    public void exportPDF(HttpServletResponse response)
            throws IOException, DocumentException {
        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=attendance.pdf"
        );

        List<Attendance> attendanceList =
                attendanceService.getAllAttendance();

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        Paragraph title = new Paragraph(
                "AI Smart Attendance Report",
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18
                )
        );

        title.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{
                1.0f,
                3.5f,
                2.5f,
                2.5f,
                2.5f,
                2.5f,
                2.0f
        });

        String[] headers = {
                "ID",
                "Student Name",
                "USN",
                "Department",
                "Date",
                "Time",
                "Status"
        };

        for (String header : headers) {

            PdfPCell cell = new PdfPCell(
                    new Phrase(
                            header,
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD)
                    )
            );

            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(cell);

        }

        for (Attendance attendance : attendanceList) {

            table.addCell(String.valueOf(attendance.getId()));

            table.addCell(attendance.getStudent().getName());

            table.addCell(attendance.getStudent().getUsn());

            table.addCell(attendance.getStudent().getDepartment());

            table.addCell(attendance.getAttendanceDate().toString());

            table.addCell(attendance.getAttendanceTime().toString());

            table.addCell(attendance.getStatus());

        }

        document.add(table);

        document.close();

    }

}
