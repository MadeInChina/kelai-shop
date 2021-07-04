package cn.com.kelaikewang.infrastructure.dao;

import org.broadleafcommerce.common.util.dao.TQJoin;
import org.broadleafcommerce.common.util.dao.TQOrder;
import org.broadleafcommerce.common.util.dao.TQRestriction;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class BaseDaoImpl<T> implements BaseDao<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;
    @Override
    public T create(T model) {
        em.persist(model);
        return model;
    }

    @Override
    public void update(T model) {
        em.merge(model);
    }

    @Override
    public void delete(T model) {
        em.remove(model);
    }

    @Override
    public void delete(long id) {
        T model = getById(id);
        em.remove(model);
    }

    @Override
    public void delete(QueryConditions queryConditions) {
        T item = getByQueryConditions(queryConditions);
        delete(item);
    }

    @Override
    public T getById(long id) {
        try {
            return (T) em.find(Class.forName(getModelClass().getName() + "Impl"), id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据id获取实体异常",e);
            return null;
        }
    }

    @Override
    public T getByQueryConditions(QueryConditions queryConditions) {
        if (queryConditions == null || queryConditions.getRestrictions() == null || queryConditions.getRestrictions().size() == 0){
            throw new IllegalArgumentException("查询条件不能为空");
        }
        TypedQueryBuilder<T> queryBuilder = new TypedQueryBuilder<>(getModelClass(), queryConditions.getRootAlias());
        for (TQRestriction restriction : queryConditions.getRestrictions()){
            queryBuilder.addRestriction(restriction);
        }
        return queryBuilder.toQuery(em).getSingleResult();

    }

    @Override
    public List<T> listByQueryConditions(QueryConditions queryConditions) {
        if (queryConditions == null){
            throw new IllegalArgumentException("queryConditions不能为null");
        }
        TypedQueryBuilder<T> queryBuilder = new TypedQueryBuilder<>(getModelClass(), queryConditions.getRootAlias());
        for (TQRestriction restriction : queryConditions.getRestrictions()){
            queryBuilder.addRestriction(restriction);
        }
        for (TQOrder order : queryConditions.getOrders()){
            queryBuilder.addOrder(order);
        }
        for (TQJoin join : queryConditions.getJoins()){
            queryBuilder.addJoin(join);
        }

        return queryBuilder.toQuery(em).getResultList();
    }

    @Override
    public List<T> listAll() {
        TypedQueryBuilder<T> queryBuilder = new TypedQueryBuilder<>(getModelClass(), "q");
        return queryBuilder.toQuery(em).getResultList();
    }

    @Override
    public long countByQueryConditions(QueryConditions queryConditions) {
        if (queryConditions == null){
            throw new IllegalArgumentException("queryConditions不能为null");
        }
        TypedQueryBuilder<T> queryBuilder = new TypedQueryBuilder<>(getModelClass(), queryConditions.getRootAlias());
        for (TQRestriction restriction : queryConditions.getRestrictions()){
            queryBuilder.addRestriction(restriction);
        }
        return queryBuilder.toCountQuery(em).getSingleResult();

    }

    @Override
    public List<T> pagination(QueryConditions queryConditions, int pageIndex, int pageSize) {
        if (queryConditions == null){
            throw new IllegalArgumentException("queryConditions不能为null");
        }
        TypedQueryBuilder<T> queryBuilder = new TypedQueryBuilder<>(getModelClass(), queryConditions.getRootAlias());
        for (TQRestriction restriction : queryConditions.getRestrictions()){
            queryBuilder.addRestriction(restriction);
        }
        for (TQOrder order : queryConditions.getOrders()){
            queryBuilder.addOrder(order);
        }
        for (TQJoin join : queryConditions.getJoins()){
            queryBuilder.addJoin(join);
        }
        return queryBuilder.toQuery(em)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex-1) * pageSize)
                .getResultList();
    }
    @Override
    public abstract Class<T> getModelClass();

    @Override
    public void flush() {
        em.flush();
    }
}
