package cn.stylefeng.guns.core.shiro;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShiroUtils {
    //遍历同一个账户的session
    private List<Session> getLoginedSession(Subject currentUser) {
        Collection<Session> list = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO()
                .getActiveSessions();
        List<Session> loginedList = new ArrayList<Session>();
        Object loginUser =  currentUser.getPrincipal();
        for (Session session : list) {

            Subject s = new Subject.Builder().session(session).buildSubject();

            if (s.isAuthenticated()) {
                Object user =  s.getPrincipal();
                System.out.println(user.toString());
//                if (user.getUsername().equalsIgnoreCase(loginUser.getUsername())) {
//                    if (!session.getId().equals(
//                            currentUser.getSession().getId())) {
//                        loginedList.add(session);
//                    }
//                }
            }
        }
        return loginedList;
    }
}
