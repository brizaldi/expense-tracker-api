package com.brizaldi.expensetracker.repositories;

import com.brizaldi.expensetracker.domain.User;
import com.brizaldi.expensetracker.exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
