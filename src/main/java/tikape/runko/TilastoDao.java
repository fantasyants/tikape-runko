
package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TilastoDao implements Dao<Tilasto, Integer> {
    private Database database;

    public TilastoDao(Database database) {
        this.database = database;

    }

    @Override
    public Tilasto findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tilasto> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tilasto save(Tilasto object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public List<Tilasto> findCount() throws SQLException {
        String query = "SELECT RaakaAine.nimi AS nimi, COUNT(Resepti.id) AS lkm\n"
                + "FROM RaakaAine\n"
                + "LEFT JOIN RaakaAineResepti ON RaakaAineResepti.raakaAine_nimi = RaakaAine.nimi\n"
                + "LEFT JOIN Resepti ON RaakaAineResepti.resepti_id = Resepti.id\n"
                + "GROUP BY RaakaAine.nimi\n"
                + "ORDER BY RaakaAine.nimi;";
        List<Tilasto> list = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(new Tilasto(result.getString("nimi"), result.getInt("lkm")));
            }
        }

        return list;
    }
}
