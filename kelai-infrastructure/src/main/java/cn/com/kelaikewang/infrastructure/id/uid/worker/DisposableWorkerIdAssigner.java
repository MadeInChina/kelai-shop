/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.kelaikewang.infrastructure.id.uid.worker;

import cn.com.kelaikewang.infrastructure.id.uid.worker.dao.WorkerNodeDao;
import cn.com.kelaikewang.infrastructure.id.uid.worker.domain.WorkerNode;
import cn.com.kelaikewang.infrastructure.id.uid.worker.domain.WorkerNodeImpl;
import cn.com.kelaikewang.infrastructure.id.uid.utils.DockerUtils;
import cn.com.kelaikewang.infrastructure.id.uid.utils.NetUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Represents an implementation of {@link WorkerIdAssigner}, 
 * the worker id will be discarded after assigned to the UidGenerator
 * 
 * @author yutianbao
 */

public class DisposableWorkerIdAssigner implements WorkerIdAssigner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableWorkerIdAssigner.class);

    @Autowired
    private WorkerNodeDao workerNodeRepository;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     * 
     * @return assigned worker id
     */
    @Transactional("blTransactionManager")
    public long assignWorkerId() throws Exception {
        // build worker node entity
        WorkerNodeImpl workerNodeEntity = buildWorkerNode();
        Date now = new Date();
        workerNodeEntity.setCreated(now);
        workerNodeEntity.setModified(now);
        // add worker node for new (ignore the same IP + PORT)
        WorkerNode response = workerNodeRepository.save(workerNodeEntity);
        //workerNodeRepository.flush();
        LOGGER.info("Add worker node:" + workerNodeEntity);

        return response.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    protected WorkerNodeImpl buildWorkerNode() {
        WorkerNodeImpl workerNodeEntity = new WorkerNodeImpl();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());

        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        }

        return workerNodeEntity;
    }

}
