package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

//インデックスページを表示するためにリストを取得するためのサーブレット
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public IndexServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //詳細画面から一覧に戻った際にセッションスコープを消す
        if(request.getSession().getAttribute("task_id") != null) {
            request.getSession().removeAttribute("task_id");
            System.out.println("呼ばれた");
        }

        //タスク一覧を取得し、リクエストのセットする
        EntityManager em = DBUtil.createEntityManager();
        List<Task> tasks = em.createNamedQuery("getAllTasks",Task.class).getResultList();
        em.close();
        request.setAttribute("tasks", tasks);

        //ページ遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        rd.forward(request, response);
    }

}
