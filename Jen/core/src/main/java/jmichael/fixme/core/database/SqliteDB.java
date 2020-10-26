package jmichael.fixme.core.database;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;

import java.sql.*;

import static jmichael.fixme.core.FixHandler.BRIGHT_GREEN;

public class SqliteDB {
    private static final String DBURL = "jdbc:sqlite:/home/jen/IdeaProjects/finalfix/Jen/core/resources/transactions.db";
    private static Connection connection;

    private static void connectDB() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DBURL);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error :: Database connection failed " + e.getMessage());
        }
        connection = conn;
    }

    private static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = null;
        } catch (SQLException e) {
            System.out.println("Database closing connection failed");
        }
    }

    private static Connection getConnection() {
        if (connection == null) {
            connectDB();
        }
        return connection;
    }

    public static void insertData(String marketName, String brokerName, String operation, String stockItem,
                                  String price, String quantity, String response, String message) {
        final Connection localConnection = getConnection();
        if (localConnection != null) {
            try (final PreparedStatement pstmt = localConnection.prepareStatement("INSERT INTO transactions(market, broker, operation, stockItem, " +
                    "price, quantity, response, message) VALUES(?,?,?,?,?,?,?,?)")) {
                pstmt.setString(1, marketName);
                pstmt.setString(2, brokerName);
                pstmt.setString(3, operation);
                pstmt.setString(4, stockItem);
                pstmt.setString(5, price);
                pstmt.setString(6, quantity);
                pstmt.setString(7, response);
                pstmt.setString(8, message);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("DBError :: failed to insert " + e.getMessage());
            } finally {
                closeConnection();
            }
        }
    }

    public static void selectAll() {
        final Connection localConnection = getConnection();
        if (localConnection != null) {
            try (final Statement stmt = localConnection.createStatement();
                final ResultSet rs = stmt.executeQuery("SELECT * FROM transactions")) {
                final AsciiTable table = new AsciiTable();
                System.out.println(BRIGHT_GREEN + "Transaction Log :");
                table.addRule();
                table.addRow("ID", "Market", "Broker", "Buy/Sell", "Stock_Traded", "Lot_Size", "Response", "Outcome");
                while (rs.next()) {
                    table.addRule();
                    table.addRow(rs.getInt("id"),
                            rs.getString("market"),
                            rs.getString("broker"),
                            rs.getString("operation"),
                            rs.getString("stockItem"),
                            rs.getString("quantity"),
                            rs.getString("response"),
                            rs.getString("message"));
                }
                table.addRule();
                table.getRenderer().setCWC(new CWC_LongestLine());
                System.out.println(table.render());
            } catch (SQLException e) {
                System.out.println("DBError :: Failed to select");
            } finally {
                closeConnection();
            }
        }
    }
}
