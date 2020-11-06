package banking;

import java.sql.Connection;
import org.sqlite.SQLiteDataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    String url;
    SQLiteDataSource dataSource = new SQLiteDataSource();

    public Database(String name) {
        this.url = "jdbc:sqlite:" + name;
        this.dataSource.setUrl(this.url);
    }

    public void createTable() {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToTable(String number, String pin, int balance) {
        String sql = "INSERT INTO card(number, pin, balance) VALUES(?,?,?)";

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement pstatement = con.prepareStatement(sql)) {
                // Statement execution
                pstatement.setString(1, number);
                pstatement.setString(2, pin);
                pstatement.setInt(3, balance);
                pstatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTable(String number, int balance) {
        String sql = "UPDATE card SET balance = ? WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement pstatement = con.prepareStatement(sql)) {
                // Statement execution
                pstatement.setInt(1, balance);
                pstatement.setString(2, number);
                pstatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromTable(String number) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement pstatement = con.prepareStatement(sql)) {
                // Statement execution
                pstatement.setString(1, number);
                pstatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkForNumber(String number) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT (count(*) > 0) as found FROM card WHERE number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, number);

            try (ResultSet rs = pst.executeQuery()) {
                // Only expecting a single result
                if (rs.next()) {
                    return !rs.getBoolean(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int getAvailableBalance(String number) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT balance FROM card WHERE number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, number);

            try (ResultSet rs = pst.executeQuery()) {
                // Only expecting a single result
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getCardNumber(String number) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT number FROM card WHERE number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, number);

            try (ResultSet rs = pst.executeQuery()) {
                // Only expecting a single result
                if (rs.next()) {
                    return rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPin(String number) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT pin FROM card WHERE number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, number);

            try (ResultSet rs = pst.executeQuery()) {
                // Only expecting a single result
                if (rs.next()) {
                    return rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
