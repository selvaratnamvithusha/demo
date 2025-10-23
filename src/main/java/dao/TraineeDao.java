package dao;

import db.TraineeDB;
import model.Trainee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TraineeDao {
    public boolean addTrainee(Trainee t) {
        String sql = "INSERT INTO trainees(name,email,department,stipend) VALUES(?,?,?,?)";
        try (Connection conn = TraineeDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getName());
            stmt.setString(2, t.getEmail());
            stmt.setString(3, t.getDepartment());
            stmt.setDouble(4, t.getStipend());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> list = new ArrayList<>();
        String sql = "SELECT * FROM trainees";

        try (Connection conn = TraineeDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Trainee t = new Trainee();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setEmail(rs.getString("email"));
                t.setDepartment(rs.getString("department"));
                t.setStipend(rs.getDouble("stipend"));

                list.add(t);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Trainee getTraineeById(int id) {
        String sql="SELECT * FROM trainees WHERE id=?";
        try(Connection conn=TraineeDB.getConnection();
        PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            ResultSet rs= stmt.executeQuery();
            if(rs.next()){
                Trainee t= new Trainee();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setEmail(rs.getString("email"));
                t.setDepartment(rs.getString("department"));
                t.setStipend(rs.getDouble("stipend"));
                return t;
            }
        } catch (Exception e){
           e.printStackTrace();
        }
        return null;
    }

    public boolean updateTrainee(Trainee t){
        String sql="UPDATE trainees SET name=?,email=?,department=?,stipend=? WHERE id=?";

        try(Connection conn=TraineeDB.getConnection();
        PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setString(1,t.getName());
            stmt.setString(2, t.getEmail());
            stmt.setString(3,t.getDepartment());
            stmt.setDouble(4,t.getStipend());
            stmt.setInt(5,t.getId());

            return stmt.executeUpdate()>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }
    }

    public boolean deleteTrainee(int id){
        String sql="DELETE FROM trainees WHERE id=?";
        try(Connection conn=TraineeDB.getConnection();
        PreparedStatement stmt= conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            return  stmt.executeUpdate()>0;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }
}

