import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface AttendanceService extends Remote {
    String markAttendance(String studentId, String status) throws RemoteException;

    String getAttendanceHistory(String studentId) throws RemoteException;

    double getAttendancePercentage(String studentId) throws RemoteException;

    Map<String, String> getAllAttendanceRecords() throws RemoteException;
}
