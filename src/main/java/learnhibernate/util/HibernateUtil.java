// Declares the package name matching the folder structure
package learnhibernate.util;

// Imports required Hibernate core classes for configuration and session management
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class to manage and provide a single instance of Hibernate SessionFactory.
 * Follows the Singleton pattern principles for resource efficiency.
 */
public class HibernateUtil {

    // Holds the single, shared instance of SessionFactory for the entire application
    private static SessionFactory sessionFactory;

    // Static initializer block runs exactly once when the class is first loaded into memory
    static {
        try {
            // Ensures the factory is only initialized if it does not already exist
            if (sessionFactory == null) {
                // 1. Instantiates a new Configuration object
                // 2. Loads settings and mappings from the "hibernate.cfg.xml" file
                // 3. Builds and returns the heavy SessionFactory instance
                sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            }
        } catch (Exception e) {
            // Logs the error and halts application startup if database initialization fails
            throw new RuntimeException("Error in creating sessionFactory " + e.getMessage());
        }
    }

    /**
     * Provides global access to the initialized Hibernate SessionFactory.
     * @return The active SessionFactory instance used to open database sessions.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
