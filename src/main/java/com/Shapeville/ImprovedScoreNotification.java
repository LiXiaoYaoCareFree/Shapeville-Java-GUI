package com.Shapeville;

import javax.swing.*;

public class ImprovedScoreNotification {

    public static void showMessageDialog(int score) {
        // HTML 内容
        String htmlContent = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f9ff; }" +
                ".notification { padding: 20px; border-radius: 10px; background-color: #fff; text-align: center; box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1); position: relative; overflow: hidden; }" +
                ".title { font-size: 24px; color: #28a745; }" +
                ".score { font-size: 20px; color: #007bff; }" +
                ".confetti { position: absolute; width: 10px; height: 10px; background-color: #ff0; animation: fall 3s ease-in infinite; }" +
                "@keyframes fall { 0% { transform: translateY(-100px) rotate(0deg); opacity: 1; } 100% { transform: translateY(500px) rotate(360deg); opacity: 0; } }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='notification'>" +
                "<div class='title'>Correct!</div>" +
                "<div class='score'>Your current score: <span id='scoreValue'>" + score + "</span></div>" +
                "</div>" +
                "<script>" +
                "function createConfetti() {" +
                "  for (let i = 0; i < 50; i++) {" +
                "    let confetti = document.createElement('div');" +
                "    confetti.className = 'confetti';" +
                "    confetti.style.left = Math.random() * 100 + 'vw';" +
                "    confetti.style.width = Math.random() * 10 + 5 + 'px';" +
                "    confetti.style.height = Math.random() * 10 + 5 + 'px';" +
                "    confetti.style.animationDuration = Math.random() * 3 + 2 + 's';" +
                "    document.body.appendChild(confetti);" +
                "  }" +
                "}" +
                "createConfetti();" +
                "</script>" +
                "</body>" +
                "</html>";

        // 创建 JEditorPane 来显示 HTML 内容
        JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);  // 禁止编辑
        JScrollPane scrollPane = new JScrollPane(editorPane);  // 添加滚动条支持

        // 创建弹出框
        JOptionPane.showMessageDialog(null, scrollPane, "Score Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // 模拟调用显示通知
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showMessageDialog(10);  // 显示分数为 10 的通知
            }
        });
    }
}
