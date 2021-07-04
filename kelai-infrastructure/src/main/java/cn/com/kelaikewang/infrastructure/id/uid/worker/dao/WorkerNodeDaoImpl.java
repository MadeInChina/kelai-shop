package cn.com.kelaikewang.infrastructure.id.uid.worker.dao;

import cn.com.kelaikewang.infrastructure.id.uid.worker.domain.WorkerNode;
import cn.com.kelaikewang.infrastructure.id.uid.worker.domain.WorkerNodeImpl;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository("zjcmsWorkerNodeDao")
public class WorkerNodeDaoImpl implements WorkerNodeDao{

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Override
    public WorkerNode findByHostNameAndPort(String host, String port) {
        TypedQuery<WorkerNode> query = new TypedQueryBuilder<WorkerNode>(WorkerNode.class, "wn")
                .addRestriction("wn.hostName", "=", host)
                .addRestriction("wn.port","=",port)
                .toQuery(em).setMaxResults(1);
        List<WorkerNode> workerNodes = query.getResultList();
        if (workerNodes == null || workerNodes.size() == 0){
            return null;
        }else {
            return workerNodes.get(0);
        }
    }

    @Override
    public WorkerNode save(WorkerNode workerNode) {
        return em.merge(workerNode);
    }

    @Override
    public WorkerNode getById(Long id) {
        return em.find(WorkerNodeImpl.class,id);
    }
}
