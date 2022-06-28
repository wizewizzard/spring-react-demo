class CommentService{
    commentsURL = "/api/v1/posts/{postId}/comments";

    getCommentsByPostId(postId, pageParams = {}){
        console.log('Fetching comments for post ', postId);
        const {page = 0, size = 5} = pageParams;
        return fetch(this.commentsURL.replace('{postId}', postId) + '?' + new URLSearchParams({
            page, size
        }),
        {
            headers: {
              'Accept': 'application/json'
            }
        }
        );
    }

    publishComment(postId, comment){
        console.log('Publishing comment for post ', postId, comment);
        return fetch(this.commentsURL.replace('{postId}', postId),
        {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(comment)
        }
        );
    }

    deleteComment(postId, commentId){
        return fetch(this.commentsURL.replace('{postId}', postId) + `/${commentId}`,
        {
            method: 'DELETE',
        }
        );
    }
}

export default new CommentService;