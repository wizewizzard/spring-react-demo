import PostService from "../service/PostService";


const fetchPost = async (postId) => {
    const response =  await PostService.getPost(postId);
    const data = await response.json();
    return data;      
}

export default fetchPost;