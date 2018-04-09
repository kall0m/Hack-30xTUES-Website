package hacktuesApp.services;

import hacktuesApp.models.Post;
import hacktuesApp.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("postService")
public class PostServiceStubImpl implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceStubImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public boolean postExists(Integer id) {
        return postRepository.exists(id);
    }

    public Post findPost(Integer id) {
        return postRepository.findOne(id);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public void savePost(Post post) {
        postRepository.saveAndFlush(post);
    }
}
