class PostService{

    postsURL = "/api/v1/posts";

    getPosts(params){
        console.log('Called fetch with params ', params)
        return fetch(this.postsURL + '?' + new URLSearchParams({
            page: params.page,
            size: params.pageSize
        }),
        {
            headers: {
              'Accept': 'application/json'
            }
        }
        );
    }

    getPost(postId){
        console.log('Called fetch with params ', postId)
        return fetch(this.postsURL + "/" + postId,
        {
            headers: {
              'Accept': 'application/json'
            }
        }
        );
    }

    createPost(formData){
        console.log('Creating post: ', formData);
        return fetch(this.postsURL, {
            method: 'POST',
            headers: {
                "Accept": "application/json"
            },
            body: formData
        });
    }

    deletePost(postId){
        console.log('Deleting post: ', postId);
        return fetch(this.postsURL  + "/" + postId, {
            method: 'DELETE',
            headers: {
                "Accept": "application/json"
            }
        });
    }

    updatePost(postId, formData){
        console.log('Updating post: ', formData);
        return fetch(this.postsURL  + "/" + postId, {
            method: 'PUT',
            headers: {
                "Accept": "application/json"
            },
            body: formData
        });
    }
}

export default new PostService();