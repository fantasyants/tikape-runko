package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReseptiDao implements Dao<Resepti, Integer> {

    private Database database;

    public ReseptiDao(Database database) {
        this.database = database;

    }

    @Override
    public Resepti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Resepti WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Resepti r = new Resepti(key, rs.getString("nimi"));

        rs.close();
        stmt.close();
        connection.close();

        return r;
    }

    @Override
    public List<Resepti> findAll() throws SQLException {
        List<Resepti> reseptit = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Resepti");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int i = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Resepti resepti = new Resepti(i, nimi);
            reseptit.add(resepti);

        }

        rs.close();
        stmt.close();
        conn.close();

        return reseptit;
    }

    @Override
    public Resepti save(Resepti object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        Resepti byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Resepti (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }

    private Resepti findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Resepti WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Resepti(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Resepti WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }


}
