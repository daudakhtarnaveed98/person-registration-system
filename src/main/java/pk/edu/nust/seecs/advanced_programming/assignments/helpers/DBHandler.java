package pk.edu.nust.seecs.advanced_programming.assignments.helpers;

// Importing libraries.
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pk.edu.nust.seecs.advanced_programming.assignments.models.Person;

// DBHandler class definition.
class DBHandler {
    // Attributes.
    private Configuration configuration;
    private ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;
    private Session session;

    // Constructors.
    DBHandler() {
        // Initializing configuration.
        this.configuration = new Configuration().addAnnotatedClass(Person.class).configure();
    }

    // Methods.
    // Method to save person.
    void register(Person person) {
        initializeSession();
        session.beginTransaction();
        session.save(person);
        session.getTransaction().commit();
        terminateSession();
    }

    // Method to update person.
    void update(Person person) {
        initializeSession();
        session.beginTransaction();
        session.update(person);
        session.getTransaction().commit();
        terminateSession();
    }

    // Method to delete person.
    void delete(String username) {
        initializeSession();
        session.beginTransaction();
        session.delete(session.get(Person.class, username));
        session.getTransaction().commit();
        terminateSession();
    }

    // Method to get person.
    Person get(String username) {
        initializeSession();
        Person person = session.get(Person.class, username);
        terminateSession();
        return person;
    }

    // Method to initialize session.
    private void initializeSession() {
        this.serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration
                        .getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        this.session = sessionFactory.openSession();
    }

    // Method to terminate session.
    private void terminateSession() {
        this.session.close();
        this.sessionFactory.close();
        this.serviceRegistry.close();
    }
}