package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

//編集ページを表示するためにデータを取得するサーブレット
@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EditServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 該当のIDのメッセージ1件のみをデータベースから取得
        EntityManager em = DBUtil.createEntityManager();
        Task task = em.find(Task.class, Integer.parseInt(request.getParameter("id")));
        em.close();

        // メッセージ情報とセッションIDをリクエストスコープに登録
        request.setAttribute("task", task);
        request.setAttribute("_token", request.getSession().getId());

        // メッセージデータが存在しているときのみ
        // メッセージIDをセッションスコープに登録
        if(task != null) {
            request.getSession().setAttribute("task_id", task.getId());
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
        rd.forward(request, response);

    }
}
