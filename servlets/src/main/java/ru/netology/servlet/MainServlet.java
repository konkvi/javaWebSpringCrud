package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  //выносим приватные для класса константы
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";
  private static final String API_POSTS = "/api/posts";
  private static final String API_POSTS_PATH = "/api/posts/\\d+";
  private static final String SLASH = "/";

  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals(GET) && path.equals(API_POSTS)) {
        controller.all(resp);
        return;
      }

      if (method.equals(GET) && path.matches(API_POSTS_PATH)) {
        final long id = Long.parseLong(path.substring(path.lastIndexOf(SLASH))+1);
        controller.getById(id, resp);
        return;
      }

      if (method.equals(POST) && path.equals(API_POSTS)) {
        controller.save(req.getReader(), resp);
        return;
      }

      if (method.equals(DELETE) && path.matches(API_POSTS_PATH)) {
        final long id = Long.parseLong(path.substring(path.lastIndexOf(SLASH)+1));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}

