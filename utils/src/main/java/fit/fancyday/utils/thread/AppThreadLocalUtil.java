package fit.fancyday.thread;

import fit.fancyday.model.user.pojos.MobileUser;

public class AppThreadLocalUtil {

    private final static ThreadLocal<MobileUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    //存入线程中
    public static void setUser(MobileUser MobileUser){
        WM_USER_THREAD_LOCAL.set(MobileUser);
    }

    //从线程中获取
    public static MobileUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    //清理
    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}
