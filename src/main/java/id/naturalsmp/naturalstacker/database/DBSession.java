package id.naturalsmp.naturalstacker.database;

import com.naturalsmp.common.databasebridge.DatabaseSessionFactory;
import com.naturalsmp.common.databasebridge.logger.ILogger;
import com.naturalsmp.common.databasebridge.session.IDatabaseSession;
import com.naturalsmp.common.databasebridge.sql.query.Column;
import com.naturalsmp.common.databasebridge.sql.query.QueryResult;
import com.naturalsmp.common.databasebridge.sql.session.MariaDBDatabaseSession;
import com.naturalsmp.common.databasebridge.sql.session.MySQLDatabaseSession;
import com.naturalsmp.common.databasebridge.sql.session.SQLDatabaseSession;
import com.naturalsmp.common.databasebridge.sql.session.SQLiteDatabaseSession;
import com.naturalsmp.common.databasebridge.transaction.DatabaseTransactionsExecutor;
import com.naturalsmp.common.databasebridge.transaction.IDatabaseTransaction;
import id.naturalsmp.naturalstacker.NaturalStacker;

import java.io.File;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class DBSession {

    private static final ILogger LOGGER = new ILogger() {
        @Override
        public void error(String message, Throwable error) {
            NaturalStacker.log(message);
            error.printStackTrace();
        }

        @Override
        public boolean hasDebugEnabled() {
            return false;
        }

        @Override
        public void debug(String message) {
            // Do nothing
        }

        @Override
        public void info(String message) {
            NaturalStacker.log(message);
        }
    };

    private static SQLDatabaseSession<?> globalSession = null;

    public static boolean isReady() {
        return globalSession != null;
    }

    public static boolean createConnection(NaturalStacker plugin) {
        SQLDatabaseSession<?> session = createSessionInternal(plugin, true);

        if (session.connect()) {
            globalSession = session;
            return true;
        }

        return false;
    }

    public static CompletableFuture<Void> execute(IDatabaseTransaction transaction) {
        return globalSession.execute(transaction);
    }

    public static CompletableFuture<Void> execute(IDatabaseTransaction... transactions) {
        return globalSession.execute(transactions);
    }

    public static CompletableFuture<Void> execute(Collection<IDatabaseTransaction> transactions) {
        return globalSession.execute(transactions);
    }

    public static void createTable(String tableName, Column... columns) {
        if (isReady())
            globalSession.createTable(tableName, columns, QueryResult.EMPTY_VOID_QUERY_RESULT);
    }

    public static void addColumn(String tableName, String columnName, String type) {
        if (isReady())
            globalSession.addColumn(tableName, columnName, type, QueryResult.EMPTY_VOID_QUERY_RESULT);
    }

    public static void select(String tableName, String filters, QueryResult<ResultSet> queryResult) {
        if (isReady())
            globalSession.select(tableName, filters, queryResult);
    }

    public static void close() {
        if (isReady()) {
            DatabaseTransactionsExecutor.stopActiveExecutors();
            globalSession.close();
        }
    }

    private static SQLDatabaseSession<?> createSessionInternal(NaturalStacker plugin, boolean logging) {
        IDatabaseSession.Args args;
        switch (plugin.getSettings().databaseType) {
            case "MYSQL":
                args = new MySQLDatabaseSession.Args(plugin.getSettings().databaseMySQLAddress,
                        plugin.getSettings().databaseMySQLPort, plugin.getSettings().databaseMySQLDBName,
                        plugin.getSettings().databaseMySQLUsername, plugin.getSettings().databaseMySQLPassword,
                        plugin.getSettings().databaseMySQLPrefix, plugin.getSettings().databaseMySQLSSL,
                        plugin.getSettings().databaseMySQLPublicKeyRetrieval,
                        plugin.getSettings().databaseMySQLWaitTimeout, plugin.getSettings().databaseMySQLMaxLifetime,
                        "NaturalStacker Database Thread", LOGGER);
                break;
            case "MARIADB":
                args = new MariaDBDatabaseSession.Args(plugin.getSettings().databaseMySQLAddress,
                        plugin.getSettings().databaseMySQLPort, plugin.getSettings().databaseMySQLDBName,
                        plugin.getSettings().databaseMySQLUsername, plugin.getSettings().databaseMySQLPassword,
                        plugin.getSettings().databaseMySQLPrefix, plugin.getSettings().databaseMySQLSSL,
                        plugin.getSettings().databaseMySQLPublicKeyRetrieval,
                        plugin.getSettings().databaseMySQLWaitTimeout, plugin.getSettings().databaseMySQLMaxLifetime,
                        "NaturalStacker Database Thread", LOGGER);
                break;
            default:
                File databaseFile = new File(plugin.getDataFolder(), "database.db");
                args = new SQLiteDatabaseSession.Args(databaseFile,
                        "NaturalStacker Database Thread", LOGGER);
                break;
        }

        SQLDatabaseSession<?> session = (SQLDatabaseSession<?>) DatabaseSessionFactory.createSession(args);
        if (logging)
            session.setLogging(true);
        return session;
    }

}
