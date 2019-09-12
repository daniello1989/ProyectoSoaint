package com.soaint.repository;

public interface IDao<T> {
	
	public T getUser(String id) throws Exception;
	public void deleteUser(String id) throws Exception;
	public void saveUser(String email, String nombre) throws Exception;
	public void saveUserOther(String contact) throws Exception;
	}//IDao
