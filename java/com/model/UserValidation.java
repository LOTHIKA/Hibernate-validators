package com.model;

import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ConstraintViolation;


public class UserValidation {
	

    private static SessionFactory sessionfactory;

    static {
        sessionfactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }
	public static void main(String[] args) {
	ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	Validator validator=validatorFactory.getValidator();
	System.out.println("Checking for invalid user data..");
	System.out.println("-------------------------------------");
	User invalidUser=new User(null, 
			"a","test123",12456,"javatechnology",
			"db","","1234","y",-2,1,new Date(),
			getPastOrFutureDate(-2),getPastOrFutureDate(2),
			5,"sample1.com","123@");
	Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);
	if(violations.isEmpty()) {
		System.out.println("Valid user data provided");
		saveUser(invalidUser);
	}else {
		System.out.println("Invalid user data found");
		for(ConstraintViolation<User> violation:violations) {
			System.out.println(violation.getMessage());
		}
	}
	

	
	System.out.println("Checking for valid user data..");
	System.out.println("-------------------------------------");
	User validUser=new User(1L, "author",
			"a@gmail.com",16,"4","3",
			"ML",null,"YN",2,0,
			getPastOrFutureDate(2),getPastOrFutureDate(1),
			getPastOrFutureDate(-2),2,"https://www.vsb.org/",
			"9784564123214");
	       violations = validator.validate(validUser);
	if(violations.isEmpty()) {
		System.out.println("Valid user data provided");
		saveUser(validUser);
	}else {
		System.out.println("Invalid user data found");
		for(ConstraintViolation<User> violation:violations) {
			System.out.println(violation.getMessage());
		}
	}
	System.out.println("---------------------------------");
	
}
	public static Date getPastOrFutureDate(int days) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	
	
	 public static void saveUser(User user) {
	        Session session = sessionfactory.openSession();
	        Transaction transaction = null;
	        
	        try {
	            transaction = session.beginTransaction();
	            session.save(user);
	            transaction.commit();
	            System.out.println("User saved to database");
	        } catch (Exception e) {
	            if (transaction != null) {
	                transaction.rollback();
	            }
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
	    }
 }

	
