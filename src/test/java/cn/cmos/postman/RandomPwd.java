package cn.cmos.postman;

import java.util.UUID;

public class RandomPwd {
    public static void main(String[] args) {
        String uid = UUID.randomUUID().toString();
        StringBuffer sb = new StringBuffer();
        String[] strs = uid.split("-");
        for(int i=0;i< strs.length;i++){
            sb.append(strs[i]);
        }
        System.out.println(uid);
        System.out.println(uid.length());
        System.out.println(sb.substring(22));
    }
}
