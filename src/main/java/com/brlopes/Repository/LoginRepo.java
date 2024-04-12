package com.brlopes.Repository;

import org.springframework.data.repository.CrudRepository;


import com.brlopes.Model.Login;

public interface LoginRepo extends CrudRepository<Login, Long> {

}
