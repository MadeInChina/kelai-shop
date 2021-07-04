package cn.com.kelaikewang.infrastructure.id.uid.worker.domain;

import java.util.Date;

public interface WorkerNode {
    long getId();

    void setId(long id);

    String getHostName();

    void setHostName(String hostName);

    String getPort();

    void setPort(String port);

    int getType();

    void setType(int type);

    Date getLaunchDate();


    Date getCreated();

    void setCreated(Date created);

    Date getModified();

    void setModified(Date modified);

    void setLaunchDate(Date launchDate);
}
