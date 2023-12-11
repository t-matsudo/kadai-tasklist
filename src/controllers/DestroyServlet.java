package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

//データの削除を行うサーブレット
@WebServlet("/destroy")
public class DestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DestroyServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            //DBからデータを検索
            EntityManager em = DBUtil.createEntityManager();
            Task task = em.find(Task.class, (Integer)(request.getSession().getAttribute("task_id")));
            //DBからデータを削除
            em.getTransaction().begin();
            em.remove(task);
            em.getTransaction().commit();

            // セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("message_id");

            // indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }

    }

}
