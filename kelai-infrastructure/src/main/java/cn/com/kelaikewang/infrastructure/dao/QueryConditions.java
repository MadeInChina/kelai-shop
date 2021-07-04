package cn.com.kelaikewang.infrastructure.dao;

import org.broadleafcommerce.common.util.dao.TQJoin;
import org.broadleafcommerce.common.util.dao.TQOrder;
import org.broadleafcommerce.common.util.dao.TQRestriction;

import java.util.ArrayList;
import java.util.List;

public class QueryConditions {
    protected String rootAlias;
    protected List<TQRestriction> restrictions = new ArrayList<>();
    protected List<TQJoin> joins = new ArrayList<>();
    protected List<TQOrder> orders = new ArrayList<>();

    public static final QueryConditions EMPTY_QUERY_CONDITIONS = new QueryConditions();


    public String getRootAlias() {
        return rootAlias;
    }

    public void setRootAlias(String rootAlias) {
        this.rootAlias = rootAlias;
    }

    public List<TQRestriction> getRestrictions() {
        return restrictions;
    }

    public List<TQJoin> getJoins() {
        return joins;
    }

    public List<TQOrder> getOrders() {
        return orders;
    }

    public void addRestriction(TQRestriction restriction){
        this.restrictions.add(restriction);
    }
    public void addJoin(TQJoin join){
        this.joins.add(join);
    }
    public void addOrder(TQOrder order){
        this.orders.add(order);
    }
}
