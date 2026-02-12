package org.example;

import com.sun.tools.javac.comp.Enter;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.cfg.Configuration;
import org.example.entity.Student;
import org.hibernate.sql.Delete;
import org.hibernate.sql.Insert;
import org.hibernate.sql.Update;
import org.hibernate.sql.ast.tree.expression.Every;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Configuration cfg = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class);

        SessionFactory factory = cfg.buildSessionFactory();
        Scanner sc = new Scanner(System.in);

        while (true) {
//            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Insert Student");
            System.out.println("2. Fetch Student by ID");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Show All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1: // INSERT
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    Session s1 = factory.openSession();
                    Transaction t1 = s1.beginTransaction();
                    try {
                        Student newStudent = new Student(id, name, email);
                        s1.persist(newStudent);
                        t1.commit();
                        System.out.println(" Student inserted successfully!");
                    } catch (Exception e) {
                        t1.rollback();
                        System.out.println(" Error: " + e.getMessage());
                    } finally {
                        s1.close();
                    }
                    break;

                case 2: // FETCH BY ID
                    System.out.print("Enter Student ID to fetch: ");
                    int fetchId = sc.nextInt();

                    Session s2 = factory.openSession();
                    try {
                        Student found = s2.get(Student.class, fetchId);
                        if (found != null)
                            System.out.println(" Found: " + found);
                        else
                            System.out.println(" No student found with ID: " + fetchId);
                    } finally {
                        s2.close();
                    }
                    break;

                case 3: // UPDATE
                    System.out.print("Enter Student ID to update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new Email: ");
                    String newEmail = sc.nextLine();

                    Session s3 = factory.openSession();
                    Transaction t3 = s3.beginTransaction();
                    try {
                        Student toUpdate = s3.get(Student.class, updateId);
                        if (toUpdate != null) {
                            toUpdate.setName(newName);
                            toUpdate.setEmail(newEmail);
                            s3.merge(toUpdate);
                            t3.commit();
                            System.out.println(" Student updated successfully!");
                        } else {
                            System.out.println(" No student found with ID: " + updateId);
                        }
                    } catch (Exception e) {
                        t3.rollback();
                        System.out.println("Error: " + e.getMessage());
                    } finally {
                        s3.close();
                    }
                    break;

                case 4: // DELETE
                    System.out.print("Enter Student ID to delete: ");
                    int deleteId = sc.nextInt();

                    Session s4 = factory.openSession();
                    Transaction t4 = s4.beginTransaction();
                    try {
                        Student toDelete = s4.get(Student.class, deleteId);
                        if (toDelete != null) {
                            s4.remove(toDelete);
                            t4.commit();
                            System.out.println(" Student deleted successfully!");
                        } else {
                            System.out.println(" No student found with ID: " + deleteId);
                        }
                    } catch (Exception e) {
                        t4.rollback();
                        System.out.println(" Error: " + e.getMessage());
                    } finally {
                        s4.close();
                    }
                    break;

                case 5: // ALL STUDENTS
                    Session s5 = factory.openSession();
                    try {
                        List<Student> allStudents = s5.createQuery("FROM Student", Student.class).list();
                        if (allStudents.isEmpty()) {
                            System.out.println(" No students found!");
                        } else {
                            System.out.println("\n===== All Students =====");
                            for (Student st : allStudents) {
                                System.out.println(st);
                            }
                        }
                    } finally {
                        s5.close();
                    }
                    break;

                case 6: // EXIT
                    System.out.println(" Goodbye!");
                    factory.close();
                    sc.close();
                    return;

                default:
                    System.out.println(" Invalid choice! Please enter 1-6.");
            }
        }
    }
}
