import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AttendanceServiceImpl extends UnicastRemoteObject implements AttendanceService {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> attendanceBook;
    private final DateTimeFormatter formatter;

    public AttendanceServiceImpl() throws RemoteException {
        super();
        attendanceBook = new ConcurrentHashMap<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String markAttendance(String studentId, String status) throws RemoteException {
        String id = normalizeStudentId(studentId);
        String cleanStatus = normalizeStatus(status);

        if (id.isEmpty()) {
            return "Error: Student ID is required.";
        }
        if (cleanStatus.isEmpty()) {
            return "Error: Select a valid status: Present or Absent.";
        }

        String time = LocalDateTime.now().format(formatter);
        String record = time + " | " + cleanStatus + " | Request handled by: "
                + Thread.currentThread().getName();

        attendanceBook.computeIfAbsent(id, key -> new CopyOnWriteArrayList<>()).add(record);

        System.out.println("[Attendance Updated] ID=" + id + ", Status=" + cleanStatus);
        return "Attendance saved successfully.\nStudent ID : " + id + "\nStatus     : " + cleanStatus;
    }

    @Override
    public String getAttendanceHistory(String studentId) throws RemoteException {
        String id = normalizeStudentId(studentId);
        if (id.isEmpty()) {
            return "Error: Student ID is required.";
        }

        CopyOnWriteArrayList<String> records = attendanceBook.get(id);
        if (records == null || records.isEmpty()) {
            return "No record found for Student ID: " + id;
        }

        int present = countStatus(records, "Present");
        int absent = countStatus(records, "Absent");

        StringBuilder report = new StringBuilder();
        report.append("Student Attendance Summary\n");
        report.append("==========================\n");
        report.append("Student ID : ").append(id).append("\n");
        report.append("Total Days : ").append(records.size()).append("\n");
        report.append("Present    : ").append(present).append("\n");
        report.append("Absent     : ").append(absent).append("\n");
        report.append("Percentage : ").append(String.format("%.2f", calculatePercentage(records))).append("%\n\n");
        report.append("Attendance History:\n");

        for (int i = 0; i < records.size(); i++) {
            report.append(i + 1).append(") ").append(records.get(i)).append("\n");
        }
        return report.toString();
    }

    @Override
    public double getAttendancePercentage(String studentId) throws RemoteException {
        String id = normalizeStudentId(studentId);
        if (id.isEmpty()) {
            return 0.0;
        }

        CopyOnWriteArrayList<String> records = attendanceBook.get(id);
        if (records == null || records.isEmpty()) {
            return 0.0;
        }
        return calculatePercentage(records);
    }

    @Override
    public Map<String, String> getAllAttendanceRecords() throws RemoteException {
        Map<String, String> allRecords = new TreeMap<>();

        for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : attendanceBook.entrySet()) {
            List<String> records = entry.getValue();
            StringBuilder details = new StringBuilder();
            details.append("Total Records: ").append(records.size()).append("\n");
            details.append("Attendance Percentage: ")
                    .append(String.format("%.2f", calculatePercentage(records))).append("%\n");

            for (String record : records) {
                details.append("   - ").append(record).append("\n");
            }
            allRecords.put(entry.getKey(), details.toString());
        }
        return allRecords;
    }

    private int countStatus(List<String> records, String status) {
        int count = 0;
        for (String record : records) {
            if (record.contains("| " + status + " |")) {
                count++;
            }
        }
        return count;
    }

    private double calculatePercentage(List<String> records) {
        if (records == null || records.isEmpty()) {
            return 0.0;
        }
        return (countStatus(records, "Present") * 100.0) / records.size();
    }

    private String normalizeStudentId(String studentId) {
        return studentId == null ? "" : studentId.trim();
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return "";
        }
        if (status.equalsIgnoreCase("Present")) {
            return "Present";
        }
        if (status.equalsIgnoreCase("Absent")) {
            return "Absent";
        }
        return "";
    }
}
