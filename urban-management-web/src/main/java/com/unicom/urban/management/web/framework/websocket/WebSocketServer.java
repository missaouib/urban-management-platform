//package com.unicom.urban.management.web.framework.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author liukai
// */
//@Slf4j
//@Component
//@ServerEndpoint(value = "/ws/asset")
//public class WebSocketServer {
//
//    private static final AtomicInteger onlineCount = new AtomicInteger(0);
//
//
//    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();
//
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        SessionSet.add(session);
//        int cnt = onlineCount.incrementAndGet(); // 在线数加1
//        log.info("有连接加入，当前连接数为：{}", cnt);
//        sendMessage(session, "连接成功");
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(Session session) {
//        SessionSet.remove(session);
//        int cnt = onlineCount.decrementAndGet();
//        log.info("有连接关闭，当前连接数为：{}", cnt);
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("来自客户端的消息：{}", message);
//        sendMessage(session, "收到消息，消息内容：" + message);
//    }
//
//
//    /**
//     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
//     *
//     * @param session
//     * @param message
//     */
//    public static void sendMessage(Session session, String message) {
//        try {
////            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            log.error("发送消息出错：{}", e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//}
//
