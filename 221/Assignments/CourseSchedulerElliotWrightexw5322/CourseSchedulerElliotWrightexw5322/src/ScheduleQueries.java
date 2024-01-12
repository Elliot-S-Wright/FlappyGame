
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ellio
 */
public class ScheduleQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getCourseCode;
    private static PreparedStatement getStatus;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getWaitlistedStudentCount;
    private static PreparedStatement getSemester;
    private static PreparedStatement promoteStudent;
    private static PreparedStatement getTime;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement getStatusByClass;    
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(String semester, String courseCode, String studentID, String status, Timestamp timestamp)
    {
        connection = DBConnection.getConnection();
        try
        {
            addScheduleEntry = connection.prepareStatement("insert into app.schedule (semester, coursecode, studentid, status, timestamp) values (?, ?, ?, ?, ?)");
            addScheduleEntry.setString(1, semester);
            addScheduleEntry.setString(2, courseCode);
            addScheduleEntry.setString(3, studentID);
            addScheduleEntry.setString(4, status);
            addScheduleEntry.setTimestamp(5, timestamp);
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getcourseCode(String semester1, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCode = new ArrayList<String>();
        try
        {
            getCourseCode = connection.prepareStatement("select coursecode from app.schedule where studentid = ? and semester = ?");
            getCourseCode.setString(1, studentID);
            getCourseCode.setString(2, semester1);
            resultSet = getCourseCode.executeQuery();
            
            while(resultSet.next())
            {
                courseCode.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCode;
    }
    
    public static ArrayList<String> getStatus(String semester1, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> status = new ArrayList<String>();
        try
        {
            getStatus = connection.prepareStatement("select status from app.schedule where studentid = ? and semester = ?");
            getStatus.setString(1, studentID);
            getStatus.setString(2, semester1);
            resultSet = getStatus.executeQuery();
            
            while(resultSet.next())
            {
                status.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return status;
    }
    
    public static String getStatusByClass(String semester1, String studentID, String courseID)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> status = new ArrayList<String>();
        try
        {
            getStatusByClass = connection.prepareStatement("select status from app.schedule where studentid = ? and semester = ? and coursecode = ?");
            getStatusByClass.setString(1, studentID);
            getStatusByClass.setString(2, semester1);
            getStatusByClass.setString(3, courseID);
            resultSet = getStatusByClass.executeQuery();
            
            while(resultSet.next())
            {
                status.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return status.get(0);
    }
    
    public static ArrayList<String> getSemester(String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> semesters = new ArrayList<String>();
        try
        {
            getSemester = connection.prepareStatement("select semester from app.schedule where studentid = ?");
            getSemester.setString(1, studentID);
            resultSet = getSemester.executeQuery();
            
            while(resultSet.next())
            {
                semesters.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return semesters;
    }
    
    public static int getScheduledStudentCount(String semester1, String code)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select count(studentID) from app.schedule where semester = ? and courseCode = ?");
            getScheduledStudentCount.setString(1, semester1);
            getScheduledStudentCount.setString(2, code);
            resultSet = getScheduledStudentCount.executeQuery();
            
            while(resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return count;
    }
    
    public static String promoteStudent(String semester1, String code)
    {
        connection = DBConnection.getConnection();
        ArrayList<Timestamp> times = new ArrayList<Timestamp>();
        ArrayList<String> students = new ArrayList<String>();
        try
        {
            getTime = connection.prepareStatement("select min(timestamp) from app.schedule where courseCode = ? and semester = ? and status = ?");
            getTime.setString(1, code);
            getTime.setString(2, semester1);
            getTime.setString(3, "waitlisted");
            resultSet = getTime.executeQuery();
            
            while(resultSet.next())
            {
                times.add(resultSet.getTimestamp(1));                               
            }      
            
            Timestamp time = times.get(0);
            promoteStudent = connection.prepareStatement("Update app.schedule set status = ? where semester = ? and courseCode = ? and timestamp = ?");
            promoteStudent.setString(1, "scheduled");
            promoteStudent.setString(2, semester1);
            promoteStudent.setString(3, code);
            promoteStudent.setTimestamp(4, time);
            promoteStudent.executeUpdate();
            
            getStudent = connection.prepareStatement("select studentID from app.schedule where timestamp = ?");
            getStudent.setTimestamp(1, time);
            resultSet = getStudent.executeQuery();
            
            while(resultSet.next())
            {
                students.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        String id = students.get(0);
        String firstName = StudentQueries.getFirstName(id);
        String lastName = StudentQueries.getLastName(id);
        return lastName + "," + firstName;
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {                 
            dropScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and coursecode = ?");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {                 
            dropStudentScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and coursecode = ? and studentID = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, courseCode);
            dropStudentScheduleByCourse.setString(3, studentID);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
