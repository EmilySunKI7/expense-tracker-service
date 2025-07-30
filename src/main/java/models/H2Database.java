package models;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class H2Database {

    public static void main ( String[] args )
    {
        H2Database app = new H2Database( );
        app.demo( );
    }

    public void demo ( )
    {
        DataSource dataSource = this.obtainDataSource( );
        // @Language ( "SQL" )  // Or comment: language=HTML — If using IntelliLang plugin in IntelliJ, for language injections. https://www.jetbrains.com/help/idea/using-language-injections.html
        String sql = """
                SELECT CURRENT_TIMESTAMP ;
                """;
        try
                (
                        Connection connection = dataSource.getConnection( );
                        PreparedStatement preparedStatement = connection.prepareStatement( sql );
                        ResultSet resultSet = preparedStatement.executeQuery( );
                )
        {
            if ( resultSet.next( ) )
            {
                OffsetDateTime offsetDateTime = resultSet.getObject( 1 , OffsetDateTime.class );
                System.out.println( "offsetDateTime.toString() = " + offsetDateTime );
            }
        } catch ( SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    public DataSource obtainDataSource ( )
    {
        org.h2.jdbcx.JdbcDataSource ds = new JdbcDataSource( );  // Implementation of `javax.sql.DataSource`, bundled with H2.
        ds.setURL( "jdbc:h2:/Users/emily/Documents/Database/H2/test"); // To not auto-close (and delete) the in-memory database, pass `DB_CLOSE_DELAY=-1`.
        ds.setUser( "EmilySun" );
        ds.setPassword( "emilysun" );
        ds.setDescription( "An example database to capture the current moment." );
        return ds;
    }

    public String getTimeStamp(){
        DataSource dataSource = this.obtainDataSource( );
        // @Language ( "SQL" )  // Or comment: language=HTML — If using IntelliLang plugin in IntelliJ, for language injections. https://www.jetbrains.com/help/idea/using-language-injections.html
        String sql = """
                SELECT CURRENT_TIMESTAMP ;
                """;
        try
                (
                        Connection connection = dataSource.getConnection( );
                        PreparedStatement preparedStatement = connection.prepareStatement( sql );
                        ResultSet resultSet = preparedStatement.executeQuery( );
                )
        {
            if ( resultSet.next( ) )
            {
                OffsetDateTime offsetDateTime = resultSet.getObject( 1 , OffsetDateTime.class );
                return offsetDateTime.toString();
            }

        } catch ( SQLException e )
        {
            throw new RuntimeException( e );
        }
        return "No Timestamp found";
    }
}
