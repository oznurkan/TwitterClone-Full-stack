package com.myproject.twitter.service;


import java.util.List;

public interface TwitterService<T, ID> {

    List<T> getAll();
    T findById(ID id);

    T replaceOrCreate(ID id, T request);

    T update(ID id, T patchRequest);

    T create(T request);

    void delete(ID id);



}
