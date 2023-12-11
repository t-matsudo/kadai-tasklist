package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

//アップデートを行うための登録を行うサーブレット
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //不正アクセス対策
        String token = request.getParameter("_token");
        if(token != null &&token.equals(request.getSession().getId())) {

            //DBから内容を取得
            EntityManager em = DBUtil.createEntityManager();
            Task task = em.find(Task.class, (Integer) (request.getSession().getAttribute("task_id")));

            //内容を取得
            String content = request.getParameter("content");
            task.setContent(content);

            // 更新日時のみ上書き
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            task.setUpdated_at(currentTime);

            //DB更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            // セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("task_id");

            // indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");

        }else {
            System.err.println("不正なアクセスによるエラー");
            response.sendRedirect(request.getContextPath() + "/index");
        }

    }

}
