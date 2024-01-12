
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
public class StudentQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllFirstNames;
    private static PreparedStatement getAllLastNames;
    private static PreparedStatement getStudentID;
    private static PreparedStatement getFirstName;
    private static PreparedStatement getLastName;
    private static PreparedStatement dropStudent;
    private static PreparedStatement dropStudentSchedule;
    private static ResultSet resultSet;
    
    public static void addStudent(String studentID, String firstName, String lastName)
    {
        connection = DBConnection.getConnection();
        try
        {
            addStudent = connection.prepareStatement("insert into app.student (studentID, firstName, lastName) values (?, ?, ?)");
            addStudent.setString(1, studentID);
            addStudent.setString(2, firstName);
            addStudent.setString(3, lastName);
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<String> firstNames = new ArrayList<String>();
        ArrayList<String> lastNames = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        try
        {
            getAllFirstNames = connection.prepareStatement("select firstname from app.student order by lastname");
            resultSet = getAllFirstNames.executeQuery();
            
            while(resultSet.next())
            {
                firstNames.add(resultSet.getString(1));
            }
            
            getAllLastNames = connection.prepareStatement("select lastname from app.student order by lastname");
            resultSet = getAllLastNames.executeQuery();
            
            while(resultSet.next())
            {
                lastNames.add(resultSet.getString(1));
            }
            
            for (int i = 0; i < lastNames.size(); i++) {
                names.add(lastNames.get(i) + "," + firstNames.get(i));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return names;
        
    }
    
    public static String getStudentID(String name)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> studentID = new ArrayList<String>();
        try
        {
            getStudentID = connection.prepareStatement("select studentID from app.student where lastname = ?");
            getStudentID.setString(1, name);
            resultSet = getStudentID.executeQuery();
            
            while(resultSet.next())
            {
                studentID.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentID.get(0);
        
    }
    
    public static String getFirstName(String id)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> firstName = new ArrayList<String>();
        try
        {
            getFirstName = connection.prepareStatement("select firstName from app.student where studentID = ?");
            getFirstName.setString(1, id);
            resultSet = getFirstName.executeQuery();
            
            while(resultSet.next())
            {
                firstName.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return firstName.get(0);
        
    }
    
    public static String getLastName(String id)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> LastName = new ArrayList<String>();
        try
        {
            getLastName = connection.prepareStatement("select lastName from app.student where studentID = ?");
            getLastName.setString(1, id);
            resultSet = getLastName.executeQuery();
            
            while(resultSet.next())
            {
                LastName.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return LastName.get(0);
        
    }
    
    public static void dropStudent(String id)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudent = connection.prepareStatement("delete from app.student where studentID = ?");
            dropStudent.setString(1, id);
            dropStudent.executeUpdate();
            
            dropStudentSchedule = connection.prepareStatement("delete from app.schedule where studentID = ?");
            dropStudentSchedule.setString(1, id);
            dropStudentSchedule.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
