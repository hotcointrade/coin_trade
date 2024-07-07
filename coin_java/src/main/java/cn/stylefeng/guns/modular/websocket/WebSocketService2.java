package cn.stylefeng.guns.modular.websocket;

import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.base.util.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocketServer服务
 * @author AnYuan
 */

@ServerEndpoint("/ask/{sid}")
@Slf4j
@Component
public class WebSocketService2 {
    private static HomeService homeService;
    private static Gson gson;
    @Autowired
    public void setHomeService(HomeService homeService) {
        System.out.println("执行seter方法");
        this.homeService = homeService;
        System.out.println(this.homeService);
    }
    @Autowired
    public void setGson(Gson gson) {
        System.out.println("执行seter方法");
        this.gson= gson;
        System.out.println(this.gson);
    }

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketService2> webSocketSet = new CopyOnWriteArraySet<WebSocketService2>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        //如果存在就先删除一个，防止重复推送消息
        for (WebSocketService2 webSocket : webSocketSet) {
            if (webSocket.sid.equals(sid)) {
                webSocketSet.remove(webSocket);
            }
        }
        webSocketSet.add(this);
        this.sid = sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //log.info("收到来" + sid + "的信息:" + message);
        if("\"headIng\"".equals(message)){
            return;
        }
        Result result = homeService.chatMsgSave(message, sid);

        if(result.getCode()==200){
            //群发消息
            for (WebSocketService2 item : webSocketSet) {
                try {
                    if (!item.sid.equals(sid)) {
                        ((ChatMsgDTO)result.getData()).setMine(false);
                    } else {
                        ((ChatMsgDTO)result.getData()).setMine(true);
                    }
                    item.sendMessage(gson.toJson(result));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }else {
            try {
                this.sendMessage(gson.toJson(result));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        message = message;
        this.session.getBasicRemote().sendText(message);
    }


//    /**
//     * 群发自定义消息
//     */
//    public static void sendInfo(String content, @PathParam("sid") String sid) throws IOException {
//
//        log.info("推送消息到" + sid + "，推送内容:" + content);
//        for (WebSocketService2 item : webSocketSet) {
//            try {
//                //这里可以设定只推送给这个sid的，为null则全部推送
//                if (sid == null) {
//                    item.sendMessage(content);
//                } else if (item.sid.equals(sid)) {
//                    item.sendMessage(content);
//                }
//            } catch (IOException ignored) {
//            }
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketService2 that = (WebSocketService2) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }
}
