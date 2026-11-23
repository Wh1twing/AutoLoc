package tn.esprit.autoloc.service;

public interface IWriteGenericService<T, K> {
    T create(T entity);
    T update(K id, T entity);
    void deleteById(K id);
}
