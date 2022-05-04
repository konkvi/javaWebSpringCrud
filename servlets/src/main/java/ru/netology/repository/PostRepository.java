package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostRepository {

  private final List<Post> posts;
  private long postId;

  public PostRepository() {
    this.posts = new CopyOnWriteArrayList<>();
    postId = 0;
  }

  public List<Post> all() {
    return posts;
  }

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
      savePost.setId(++postId);
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
