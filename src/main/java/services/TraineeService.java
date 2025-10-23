package services;

import com.google.gson.Gson;
import dao.TraineeDao;
import model.Trainee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/trainee")
public class TraineeService extends HttpServlet {
    private final Gson gson = new Gson();
    private final TraineeDao traineeDao = new TraineeDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");


        Trainee t = gson.fromJson(req.getReader(), Trainee.class);
        boolean added = traineeDao.addTrainee(t);

        resp.setStatus(added ? 201 : 500);
        resp.getWriter().write(added ? "{\"message\":\"Trainee added\"}" : "{\"error\":\"Failed to add trainee\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String idParam = req.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Trainee t = traineeDao.getTraineeById(id);
            if (t != null) {
                resp.getWriter().write(gson.toJson(t));
            } else {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Trainee not found\"}");
            }

        } else {
            List<Trainee> list = traineeDao.getAllTrainees();
            resp.getWriter().write(gson.toJson(list));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()) {
            Trainee trainee = gson.fromJson(reader, Trainee.class);

            if (trainee.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Missing trainee id\"}");
                return;
            }

            boolean isUpdated = traineeDao.updateTrainee(trainee);
            if (isUpdated) {
                resp.getWriter().write("{\"message\":\"Trainee updated successfully\"}");

            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Trainee not found\"}");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid JSON:" + e.getMessage() + "\"}");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean deleted = traineeDao.deleteTrainee(id);
        resp.setContentType("application/json");
        resp.setStatus(deleted ? 200 : 404);
        resp.getWriter().write(deleted ? "{\"message\":\"Trainee deleted\"}" : "{\"error\":\"Failed to delete trainee\"}");
    }
}