package com.fjsh.dag.flow.dagdata;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

import lombok.Data;

/**
 *
 * @Author: <fujiansheng.com>
 * @Description：
 * @Date: Created in :2019-02-24 20:38  
 * @Modified by:
 */
@Data
public class CommonData {
    private static final   ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>();
    public static synchronized void putData(String jobName){
//            int key=CommonData.concurrentHashMap.getOrDefault(1,1)+1;
//            System.out.println("jobName:"+jobName+" key:"+key);
            CommonData.concurrentHashMap.put(jobName,jobName);
    }
    public static void printData(){
        System.out.println("-----------------------");
        System.out.println("mapSize:"+concurrentHashMap.size());
        for(Map.Entry<String,String> entry:concurrentHashMap.entrySet()){
            System.out.println("key:"+entry.getKey()+" value:"+entry.getValue());
        }
    }
}
