package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private ConcurrentMap<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong count = new AtomicLong(0L);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    if (posts.containsKey(id)) {
      return Optional.of(posts.get(id));
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if (post.getId() <= count.get() && post.getId() != 0) {
      posts.put(post.getId(), post);
    } else {
      post.setId(count.incrementAndGet());
      posts.put(post.getId(), post);
    }
    return post;
  }

  public Optional<Post> removeById(long id) {
    if (posts.containsKey(id)) {
      return Optional.of(posts.remove(id));
    }
    return Optional.empty();
  }

}
