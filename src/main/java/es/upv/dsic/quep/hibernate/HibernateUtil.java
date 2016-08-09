package es.upv.dsic.quep.hibernate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author agna8685
 */
public class HibernateUtil implements Serializable {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            //sessionFactory = new AnnotationConfiguration().configure("/es/upv/dsic/quep/persistence/hibernate.cfg.xml").buildSessionFactory();
            sessionFactory = new Configuration().configure().buildSessionFactory();
            //sessionFactory = new AnnotationConfiguration().configure("es.upv.dsic.quep.persistence.hibernate.cfg.xml").buildSessionFactory();
            //sessionFactory = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    
}
