package cn.com.kelaikewang.infrastructure.id.factory.leaf;

public class LeafIdClientException extends RuntimeException {
    public LeafIdClientException(String msg){
        super(msg);
    }
    public LeafIdClientException(String msg,Throwable throwable){
        super(msg,throwable);
    }
}
