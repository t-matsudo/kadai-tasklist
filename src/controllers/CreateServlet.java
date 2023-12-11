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

//データを作成するサーブレット
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //不正アクセス対策
        String token = request.getParameter("_token");
        if(token != null && token.equals(request.getSession().getId())) {
            //DBトランザクション開始
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            //登録内容作成
            Task task = new Task();
            task.setContent((String)request.getParameter("content"));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            task.setCreated_at(currentTime);
            task.setUpdated_at(currentTime);

            //登録
            em.persist(task);
            em.getTransaction().commit();
            em.close();

            response.sendRedirect(request.getContextPath() + "/index");
        }else {
            System.err.println("不正なアクセスによるエラー");
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
