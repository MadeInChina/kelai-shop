package cn.com.kelaikewang.infrastructure.dao;

import java.util.List;

public interface BaseDao<T> {
    T create(T model);
    void update(T model);
    void delete(T model);
    void delete(long id);
    void delete(QueryConditions queryConditions);
    T getById(long id);
    T getByQueryConditions(QueryConditions queryConditions);
    List<T> listByQueryConditions(QueryConditions queryConditions);
    List<T> listAll();
    long countByQueryConditions(QueryConditions queryConditions);
    List<T> pagination(QueryConditions queryConditions, int pageIndex, int pageSize);
    Class<T> getModelClass();
    void flush();
}
