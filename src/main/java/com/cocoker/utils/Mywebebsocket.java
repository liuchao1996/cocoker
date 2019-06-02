package com.cocoker.utils;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{openId}")
@Component
public class Mywebebsocket {
    private static Logger logger = LogManager.getLogger(Mywebebsocket.class.getName());

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<Mywebebsocket> wsClientMap = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    // user
    private String currentUser;
    // @Resource
    // RedisUtil redisUtil;
    // private RedisUtil redisUtil=(RedisUtil)
    // ContextLoader.getCurrentWebApplicationContext().getBean("RedisUtil");

    // 此处是解决无法注入的关键
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        Mywebebsocket.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 当前会话session
     */
    @OnOpen
    public void onOpen(@PathParam(value = "openId") String openId, Session session, EndpointConfig config)
            throws Exception {
        System.out.println("openId:" + openId);
        currentUser = openId;//doctorId.substring(9);
        this.session = session;
        wsClientMap.add(this);
        addOnlineCount();
        logger.info(session.getId() + "有新链接加入，当前链接数为：" + wsClientMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        // String user_id=this.currentUser;
        // try {
        // //此处是解决无法注入的关键
        // redisUtil = applicationContext.getBean(RedisUtil.class);
        // if (redisUtil.exists(user_id)) {
        // String tokenold = (String) redisUtil.get(user_id);
        // System.out.println("---tokenold-"+tokenold);
        // redisUtil.remove(tokenold);
        // redisUtil.remove(user_id);
        // }
        //
        // } catch (Exception e) {
        // logger.error("IO异常-->{}", e);
        // }

        wsClientMap.remove(this);
        subOnlineCount();
        logger.info("有一链接关闭，当前链接数为:" + wsClientMap.size());
    }

    /**
     * 收到客户端消息
     *
     * @param message 客户端发送过来的消息
     * @param session 当前会话session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("来终端的警情消息:" + message);
        // sendMsgToAll(message);
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("wsClientMap发生错误!");
        error.printStackTrace();
    }

    /**
     * 给所有客户端群发消息
     *
     * @param message 消息内容
     * @throws IOException
     */
    public void sendMsgToAll(String message) throws IOException {
        for (Mywebebsocket item : wsClientMap) {
            if (item.session.isOpen()) {
                item.session.getBasicRemote().sendText(message);
            }

        }
        //logger.info("成功群送一条消息:" + wsClientMap.size());
    }

    public void sendMessage(Object message) throws Exception {
        this.session.getBasicRemote().sendText(JSON.toJSONString(message));
        logger.info("成功发送一条消息:" + message);
    }

    /**
     * 发送给指定用户
     *
     * @param message
     * @param userCode
     * @throws IOException
     */
    public static void sendMessageTo(Object message, String userCode) throws IOException {
        for (Mywebebsocket item : wsClientMap) {
            System.out.println("全部用户：" + item.currentUser);
            if (item.currentUser.equals(userCode)) {
                item.session.getBasicRemote().sendText(JSON.toJSONString(message));
                logger.info("成功发送一条消息:" + message);
            }
        }
    }

    /**
     * 匹配在线医生
     *
     * @param doctorIdL 用户编号
     * @throws IOException
     */
    public static List<String> getonlineuser(List<String> doctorIdL) throws IOException {
        List<String> newdoctorIdL = new ArrayList<>();
        for (Mywebebsocket item : wsClientMap) {
            System.out.println("全部用户：" + item.currentUser);
            for (String doctorId : doctorIdL) {
                if (item.currentUser.equals(doctorId)) {
                    newdoctorIdL.add(doctorId);
                }
            }

        }
        return newdoctorIdL;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static CopyOnWriteArraySet<Mywebebsocket> getWebSocketSet() {
        return wsClientMap;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<Mywebebsocket> webSocketSet) {
        Mywebebsocket.wsClientMap = webSocketSet;
    }

    public static synchronized int getOnlineCount() {
        return Mywebebsocket.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        Mywebebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        Mywebebsocket.onlineCount--;
    }

}
