
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
public class ClassQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet;
    
    public static void addClass(String semester, String code, int seats)
    {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement("insert into app.class (semester, coursecode, seats) values (?, ?, ?)");
            addClass.setString(1, semester);
            addClass.setString(2, code);
            addClass.setInt(3, seats);
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester1)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select coursecode from app.class where semester = ? order by coursecode");
            getAllCourseCodes.setString(1, semester1);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                courseCodes.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCodes;
        
    }
    
    public static int getClassSeats(String semester1, String code) 
    {
        connection = DBConnection.getConnection();
        ArrayList<Integer> seats = new ArrayList<Integer>();
        try
        {
            getClassSeats = connection.prepareStatement("select seats from app.class where coursecode = ? and semester = ?");
            getClassSeats.setString(1, code);
            getClassSeats.setString(2, semester1);
            resultSet = getClassSeats.executeQuery();
            
            while(resultSet.next())
            {
                seats.add(resultSet.getInt(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return seats.get(0);
        
    }
    
    public static void dropClass(String semester, String code) 
    {
        connection = DBConnection.getConnection();
        try
        {                 
            dropClass = connection.prepareStatement("delete from app.class where semester = ? and coursecode = ?");
            dropClass.setString(1, semester);
            dropClass.setString(2, code);
            dropClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
