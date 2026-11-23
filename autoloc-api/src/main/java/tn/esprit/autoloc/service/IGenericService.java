package tn.esprit.autoloc.service;

public interface IGenericService<T, K>  extends IReadGenericService<T, K>, IWriteGenericService<T, K>, ICheckGenericService<T> {

}
