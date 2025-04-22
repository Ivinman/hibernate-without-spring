package ru.aston.storage;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.aston.model.User;

import java.util.List;
import java.util.Properties;

public class UserStorageImp implements UserStorage {
    private final SessionFactory sessionFactory;
    private final Session session;

    public UserStorageImp() {
        this.sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public  UserStorageImp(Properties properties) {
        this.sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .setProperties(properties)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public void addUser(User user) {
            Transaction transaction = session.beginTransaction();
            String insert = "INSERT INTO User (name, email, age, created_at) "
                    + "values (:name, :email, :age, :created_at)";
            Query query = session.createQuery(insert);
            query.setParameter("name", user.getName());
            query.setParameter("email", user.getEmail());
            query.setParameter("age", user.getAge());
            query.setParameter("created_at", user.getCreated_at());
            query.executeUpdate();
            transaction.commit();
    }

    @Override
    public User getUser(Integer userId) {
        String get = "FROM User WHERE id = " + userId;
        Query query = session.createQuery(get);
        Object user = query.getSingleResult();
        return (User)user;
    }

    @Override
    public List<User> getUsers() {
        String getUsers = "FROM User";
        Query query = session.createQuery(getUsers);
        List<User> users = query.getResultList();
        return users;
    }

    @Override
    public void updateUser(Integer userId, User newUser) {
        Transaction transaction = session.beginTransaction();
        String update = "UPDATE User set name = :name"
                + ", email = :email"
                + ", age = :age"
                + " WHERE id = " + newUser.getId();
        Query query = session.createQuery(update);
        query.setParameter("name", newUser.getName());
        query.setParameter("email", newUser.getEmail());
        query.setParameter("age", newUser.getAge());
        query.executeUpdate();
        transaction.commit();
    }

    @Override
    public void deleteUser(Integer userId) {
        Transaction transaction = session.beginTransaction();
        String delete = "DELETE FROM User WHERE id = " + userId;
        Query query = session.createQuery(delete);
        query.executeUpdate();
        transaction.commit();
    }
}
