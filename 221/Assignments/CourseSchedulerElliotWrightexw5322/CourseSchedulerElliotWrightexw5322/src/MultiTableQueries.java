
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ellio
 */
public class MultiTableQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement getSeats;
    private static PreparedStatement getAllCourseCodes; 
    private static PreparedStatement getDescriptions;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static ResultSet resultSet;
    
    public static ArrayList<ClassDescription> getAllClassDescriptions(String currentSemester)
            
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        ArrayList<Integer> seats = new ArrayList<Integer>();
        ArrayList<ClassDescription> classDescriptions = new ArrayList<ClassDescription>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select coursecode from app.class where semester = ? order by seats");
            getAllCourseCodes.setString(1, currentSemester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                courseCodes.add(resultSet.getString(1));
            }
            
            getSeats = connection.prepareStatement("select seats from app.class where semester = ? order by seats");
            getSeats.setString(1, currentSemester);
            resultSet = getSeats.executeQuery();
            
            while(resultSet.next())
            {
                seats.add(resultSet.getInt(1));
            }                       
            
            for (int i = 0; i < courseCodes.size(); i++) {
                String description = "";
                getDescriptions = connection.prepareStatement("select description from app.course where coursecode = ?");
                getDescriptions.setString(1, courseCodes.get(i));
                resultSet = getDescriptions.executeQuery();
                if(resultSet.next()) {
                    description = resultSet.getString(1);
                }
                ClassDescription classDescription = new ClassDescription(courseCodes.get(i), description, seats.get(i));
                classDescriptions.add(classDescription);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return classDescriptions;
        
    }
    
    public static ArrayList<String> getScheduledStudentsByClass(String semester1, String code)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> students = new ArrayList<String>();
        try
        {
            getScheduledStudentsByClass = connection.prepareStatement("select studentID from app.schedule where semester = ? and coursecode = ? and status = ?");
            getScheduledStudentsByClass.setString(1, semester1);
            getScheduledStudentsByClass.setString(2, code);
            getScheduledStudentsByClass.setString(3, "scheduled");
            resultSet = getScheduledStudentsByClass.executeQuery();
            
            while(resultSet.next())
            {
                students.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return students;
    }
    
    public static ArrayList<String> getWaitlistedStudentsByClass(String semester1, String code)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> students = new ArrayList<String>();
        try
        {
            getWaitlistedStudentsByClass = connection.prepareStatement("select studentID from app.schedule where semester = ? and coursecode = ? and status = ? order by Timestamp");
            getWaitlistedStudentsByClass.setString(1, semester1);
            getWaitlistedStudentsByClass.setString(2, code);
            getWaitlistedStudentsByClass.setString(3, "waitlisted");
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            
            while(resultSet.next())
            {
                students.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return students;
    }
}
