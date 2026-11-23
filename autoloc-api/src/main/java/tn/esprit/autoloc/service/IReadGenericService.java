package tn.esprit.autoloc.service;

import java.util.List;

public interface IReadGenericService<T, K> {
    T findById(K id);
    List<T> findAll();
}
