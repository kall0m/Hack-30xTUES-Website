package hacktuesApp.services;

import hacktuesApp.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    boolean postExists(Integer id);

    Post findPost(Integer id);

    void deletePost(Post post);

    void savePost(Post post);
}
