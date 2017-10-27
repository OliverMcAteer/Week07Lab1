/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import domainmodel.Note;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 684243
 */
public class NotesDB {

    public int insert(Note note) throws NotesDBException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        try {
            String preparedQuery = "INSERT INTO Notes (ID) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ps.setString(1, note.getContents());
            int rows = ps.executeUpdate();
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(NotesDB.class.getName()).log(Level.SEVERE, "Cannont insert " + note.toString(), ex);
            throw new NotesDBException("Error inserting user");
        } finally {
            pool.freeConnection(connection);
        }

    }

    public int update(Note note) throws NotesDBException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        try {
            String preparedSQL = "UPDATE Notes SET"
                    + "Content = ?,"
                    + "dateCreated = DATETIME";

            PreparedStatement ps = connection.prepareStatement(preparedSQL);

            ps.setString(1, note.getContents());

            int rows = ps.executeUpdate();
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(NotesDB.class.getName()).log(Level.SEVERE, "Cannot update" + note.toString(), ex);
            throw new NotesDBException("Error updating Notes");
        } finally {
            pool.freeConnection(connection);
        }

    }

    public List<Note> getAll() throws NotesDBException, ParseException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT * FROM Notes");
            rs = ps.executeQuery();
            List<Note> notes = new ArrayList<Note>();
            while (rs.next()) {
                notes.add(new Note(rs.getString("ID"), rs.getString("dateCreated"), rs.getString("contents")));
            }
            pool.freeConnection(connection);
            return notes;
        } catch (SQLException ex) {
            Logger.getLogger(NotesDB.class.getName()).log(Level.SEVERE, "Cannot read Notes", ex);
            throw new NotesDBException("ERror getting NOtes");
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
            }
            pool.freeConnection(connection);

        }
    }

}
