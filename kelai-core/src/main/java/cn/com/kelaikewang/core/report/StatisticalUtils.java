package cn.com.kelaikewang.core.report;

import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("statisticalUtils")
public class StatisticalUtils {
    private StatisticalUtils(){}
    public <T> StatisticItemDTO<T> getItem(List<StatisticItemDTO<T>> data, String name){
        if (data == null){
            return null;
        }
        for (StatisticItemDTO<T> itemDTO : data){
            if (itemDTO.getName().equals(name)){
                return itemDTO;
            }
        }
        return null;
    }
}
