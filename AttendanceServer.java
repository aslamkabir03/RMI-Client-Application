import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class AttendanceServer {
    private static final int PORT = 1099;
    private static final String SERVICE = "AttendanceService";

    public static void main(String[] args) {
        try {
            try {
                LocateRegistry.createRegistry(PORT);
                System.out.println("RMI Registry started on port " + PORT + ".");
            } catch (ExportException ex) {
                System.out.println("RMI Registry already active on port " + PORT + ".");
            }

            AttendanceService attendanceService = new AttendanceServiceImpl();
            Naming.rebind("rmi://localhost:" + PORT + "/" + SERVICE, attendanceService);

            System.out.println("Distributed Attendance Server is ready.");
            System.out.println("Remote service address: rmi://localhost:" + PORT + "/" + SERVICE);
            System.out.println("Server is waiting for faculty client requests...");
        } catch (Exception e) {
            System.out.println("Server failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
