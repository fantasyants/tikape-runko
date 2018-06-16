package tikape.runko;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;

    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        RaakaAine ra = new RaakaAine(key, rs.getString("nimi"));

        rs.close();
        stmt.close();
        connection.close();

        return ra;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> ainekset = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int i = rs.getInt("id");
            String nimi = rs.getString("nimi");
            RaakaAine raakaAine = new RaakaAine(i, nimi);
            ainekset.add(raakaAine);

        }

        rs.close();
        stmt.close();
        conn.close();

        return ainekset;
    }

    @Override
    public RaakaAine save(RaakaAine raakaAine) throws SQLException {
        RaakaAine byName = findByName(raakaAine.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            stmt.setString(1, raakaAine.getNimi());
            stmt.executeUpdate();
        }

        return findByName(raakaAine.getNimi());
    }

    private RaakaAine findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new RaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public List<RaakaAine> findForResepti(Integer reseptiId) throws SQLException {
        String query = "SELECT RaakaAine.id, RaakaAine.nimi FROM RaakaAine, RaakaAineResepti\n"
                + "WHERE RaakaAine.nimi = RaakaAineResepti.raakaAine_nimi\n"
                + "AND RaakaAineResepti.Resepti_id = ?\n"
                + "ORDER BY RaakaAineResepti.jarjestys;";

        List<RaakaAine> aineet = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reseptiId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                aineet.add(new RaakaAine(result.getInt("id"), result.getString("nimi")));
            }
        }

        return aineet;
    }

}



