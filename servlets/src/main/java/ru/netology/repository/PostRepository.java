package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

  private final List<Post> posts;
  //потокобезопасный счетчик postId, я посчитал логичным начало отсчета постов с 1
  private final AtomicLong postId = new AtomicLong(1);

  public PostRepository() {
    this.posts = new CopyOnWriteArrayList<>();
    //выбрал потокобезопасный List, так как ранее был коммент на реализацию ConcurrentHashMap:
    // "нужно реализовать те методы, которые были в заглушке, именно с теми параметрами, которые были изначально.
    // вместо мапы нужно возвращать массив при получении всех элементов" => public List<Post> all() { return posts; }
    //да, CopyOnWriteArrayList коллекция подходит для частых чтений и редких записей
    // если нужна все же реализация ConcurrentMap, то ее реализацию можно посмотреть в
    // https://github.com/konkvi/javaWebSpring2.1
  }

  public List<Post> all() { return posts; }

  public Optional<Post> getById(long id) {
    for (Post post : posts) {
      if (id == post.getId()) {
        return Optional.of(post);
      }
    }
    return Optional.empty();
  }

  public Post save(Post savePost) {
    long idSavePost = savePost.getId();
    if (idSavePost == 0) {
      savePost.setId(postId.getAndIncrement());
      posts.add(savePost);
      return savePost;
    } else {
      Optional<Post> newPost = getById(idSavePost);
      if (newPost.isPresent()) {
        Post post = newPost.get();
        post.setContent(savePost.getContent());
        return post;
      } else {
        throw new NotFoundException(String.format("Пост с ID = %d невозможно сохранить!", idSavePost));
      }
    }
  }

  public void removeById(long id) {
    posts.removeIf(post -> id == post.getId());
  }
}
