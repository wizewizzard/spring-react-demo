import React, { useState, useEffect } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom';
import PostService from '../../service/PostService';
import ListComments from '../comment/ListComments';
import Loader from '../Loader';
import ServerError from '../util/ServerError';

const PostDetail = () => {
  const { postId } = useParams();
  const navigate = useNavigate();
  const [loaded, setLoaded] = useState(false);
  const [post, setPost] = useState({});
  const [serverError, setServerError] = useState(false);
  

  useEffect(() => {
    fetchPost();
  }, [])
  
  const fetchPost = async () => {
    const response =  await PostService.getPost(postId);
    if(response.status >= 200 && response.status < 300){
      const post = await response.json();
      setPost(post);
      setLoaded(true);
    }
    else if(response.status >= 400 && response.status < 500){
      navigate('/404');
    }
    else if(response.status >= 500){
      setServerError(true);
    }
  }

  const deletePost = async () => {
    const response =  await PostService.deletePost(postId);
    if(response.status == 200){
      navigate('/posts');
    }
  }

  const renderComponent = () => {
    return (
      <div className='container'>
      <div className='d-flex justify-content-end mb-3'>
        <div>
          <Link to={`/posts/edit/${postId}`} className='link-dark ms-3' >Edit post</Link>
        </div>
        <div>
        <Link to={'#'} className='link-dark ms-3' onClick={() => deletePost()}>Delete post</Link>
        </div>
      </div>
      <div className='row'>
        <div className='col-4'>
          <img className='scaled' src={`/${post.imagePath}`} alt='Post image' />
          <p className='text-center mt-3'><small className='text-muted'>{post.brief}</small></p>
        </div>
        <div className='col-8'>
          <h3>{post.title}</h3>
          <div dangerouslySetInnerHTML={{ __html: post.content }} />
        </div>
      </div>
      <hr />
      <ListComments postId={postId} />
    </div>
    );
  }

  return (
    serverError ? <ServerError message={'Server error occured. Unable to load post.'}/>: loaded ? renderComponent(): <Loader />
  )
}

export default PostDetail;